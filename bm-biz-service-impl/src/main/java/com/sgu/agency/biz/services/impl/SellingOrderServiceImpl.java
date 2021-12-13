package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.FileReaderUtil;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.dao.ISellingOrderDao;
import com.sgu.agency.dal.data.DateRevenueDetail;
import com.sgu.agency.dal.data.MonthRevenueDetail;
import com.sgu.agency.dal.data.YearRevenueDetail;
import com.sgu.agency.dal.entity.SellingOrder;
import com.sgu.agency.dal.entity.SellingTransaction;
import com.sgu.agency.dal.entity.Product;
import com.sgu.agency.dal.entity.ProductDetail;
import com.sgu.agency.dal.repository.ISellingOrderRepository;
import com.sgu.agency.dal.repository.ISellingTransactionRepository;
import com.sgu.agency.dal.repository.IProductDetailRepository;
import com.sgu.agency.dal.repository.IProductRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;
import com.sgu.agency.mappers.*;
import com.sgu.agency.services.ISellingOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellingOrderServiceImpl implements ISellingOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SellingOrderServiceImpl.class);

    @Autowired
    private ISellingOrderRepository sellingOrderRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ISellingTransactionRepository sellingTransactionRepository;
    @Autowired
    private IProductDetailRepository productDetailRepository;
    @Autowired
    private ISellingOrderDao sellingOrderDao;

    @Override
    @Transactional
    public List<SellingOrderDto> findAll() {
        List<SellingOrder> sellingOrders = sellingOrderRepository.findAll();
        return ISellingOrderDtoMapper.INSTANCE.toSellingOrderDtos(sellingOrders);
    }
    @Override
    public BaseSearchDto<List<SellingOrderDto>> findAll(BaseSearchDto<List<SellingOrderDto>> searchDto) {
        if(searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll());
            return searchDto;
        }

        searchDto.setSortBy("invoiceDate");
        searchDto.setSortAsc(false);
        Sort sort = null;
        if(searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<SellingOrder> page = sellingOrderRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ISellingOrderDtoMapper.INSTANCE.toSellingOrderDtos(page.getContent()));

        return searchDto;
    }

    @Override
    public List<SellingTransaction> getListSellingTransactionBySellingOrderId(String id) {
        List<SellingTransaction> sellingTransactions = sellingTransactionRepository.getListBySellingOrderId(id);
        return sellingTransactions;
    }

    @Override
    @Transactional
    public SellingOrderFullDto insert(SellingOrderFullDto sellingOrderFullDto) {
        try {
            sellingOrderFullDto.calculateTotal();
            sellingOrderFullDto.setId(UUIDHelper.generateType4UUID().toString());
            SellingOrder selling = ISellingOrderDtoMapper.INSTANCE.toSellingOrder(sellingOrderFullDto);
            selling = this.sellingOrderRepository.save(selling);

            List<SellingTransaction> sellingTransactions = ISellingTransactionDtoMapper
                    .INSTANCE.toSellingTransactions(sellingOrderFullDto.getSellingTransactions());

            for (SellingTransaction sellingTransaction : sellingTransactions)
            {
                sellingTransaction.setId(UUIDHelper.generateType4UUID().toString());
                sellingTransaction.setSellingOrder(selling);

                ProductDetail productDetail = productDetailRepository.getOne(sellingTransaction.getProductDetail().getId());
                ProductDetailDto productDetailDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(productDetail);
                productDetailDto.setQuantity(productDetailDto.getQuantity() - sellingTransaction.getQuantity());
                productDetailDto.setSellingPrice(sellingTransaction.getPrice());

                Product product = productRepository.getOne(sellingTransaction.getProductDetail().getProduct().getId());
                product.setQuantity(product.getQuantity() - sellingTransaction.getQuantity());
                this.productRepository.save(product);

                this.productDetailRepository.save(IProductDetailDtoMapper.INSTANCE.toProductDetail(productDetailDto));
            }
            this.sellingTransactionRepository.saveAll(sellingTransactions);

            return sellingOrderFullDto;

        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public SellingOrderFullDto update(SellingOrderFullDto sellingOrderFullDto) {
        try {
            sellingOrderFullDto.calculateTotal();
            SellingOrder selling = ISellingOrderDtoMapper.INSTANCE.toSellingOrder(sellingOrderFullDto);
            selling = this.sellingOrderRepository.save(selling);

            // reduce quantity
            List<SellingTransaction> oldTrans = sellingTransactionRepository.getListBySellingOrderId(sellingOrderFullDto.getId());
            for (SellingTransaction sellingTransaction : oldTrans)
            {
                ProductDetail productDetail = productDetailRepository.getOne(sellingTransaction.getProductDetail().getId());
                ProductDetailDto productDetailDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(productDetail);
                productDetailDto.setQuantity(productDetailDto.getQuantity() + sellingTransaction.getQuantity());
                productDetailDto.setSellingPrice(sellingTransaction.getPrice());

                Product product = productRepository.getOne(sellingTransaction.getProductDetail().getProduct().getId());
                product.setQuantity(product.getQuantity() + sellingTransaction.getQuantity());
                this.productRepository.save(product);

                this.productDetailRepository.save(IProductDetailDtoMapper.INSTANCE.toProductDetail(productDetailDto));
            }
            this.sellingTransactionRepository.deleteAll(oldTrans);

            // save - add new quantity
            List<SellingTransaction> sellingTransactions = ISellingTransactionDtoMapper
                    .INSTANCE.toSellingTransactions(sellingOrderFullDto.getSellingTransactions());
            for (SellingTransaction sellingTransaction : sellingTransactions)
            {
                sellingTransaction.setId(UUIDHelper.generateType4UUID().toString());
                sellingTransaction.setSellingOrder(selling);

                ProductDetail productDetail = productDetailRepository.getOne(sellingTransaction.getProductDetail().getId());
                ProductDetailDto productDetailDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(productDetail);
                productDetailDto.setQuantity(productDetailDto.getQuantity() - sellingTransaction.getQuantity());
                productDetailDto.setSellingPrice(sellingTransaction.getPrice());

                Product product = productRepository.getOne(sellingTransaction.getProductDetail().getProduct().getId());
                product.setQuantity(product.getQuantity() - sellingTransaction.getQuantity());
                this.productRepository.save(product);

                this.productDetailRepository.save(IProductDetailDtoMapper.INSTANCE.toProductDetail(productDetailDto));
            }
            this.sellingTransactionRepository.saveAll(sellingTransactions);

            return sellingOrderFullDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            sellingOrderRepository.deleteById(id);
            List <SellingTransaction> sellingTransactions = getListSellingTransactionBySellingOrderId(id);
            for (SellingTransaction sellingTransaction : sellingTransactions) {
                ProductDetail productDetail = productDetailRepository.getOne(sellingTransaction.getProductDetail().getId());
                ProductDetailDto productDetailDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(productDetail);
                productDetailDto.setQuantity(productDetailDto.getQuantity() + sellingTransaction.getQuantity());

                Product product = productRepository.getOne(sellingTransaction.getProductDetail().getProduct().getId());
                product.setQuantity(product.getQuantity() + sellingTransaction.getQuantity());
                this.productRepository.save(product);

                this.productDetailRepository.save(IProductDetailDtoMapper.INSTANCE.toProductDetail(productDetailDto));
            }
            sellingTransactionRepository.deleteAll(sellingTransactions);

            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }
    @Override
    public SellingOrderFullDto getSellingOrderFullById(String id)
    {
        SellingOrder sellingOrder = sellingOrderRepository.findById(id).get();
        List<SellingTransaction> sellingTransactions = sellingTransactionRepository.getListBySellingOrderId(id);
        SellingOrderFullDto sellingOrderFullDto = ISellingOrderDtoMapper.INSTANCE.toSellingOrderFullDto(sellingOrder);

        sellingOrderFullDto.setSellingTransactions(ISellingTransactionDtoMapper.INSTANCE.toSellingTransactionDtos(sellingTransactions));

        for (SellingTransactionDto product : sellingOrderFullDto.getSellingTransactions()) {
            if (product.getProductDetail().getImageByte() != null) {
                product.getProductDetail().setImageByte(FileReaderUtil.decompressBytes(product.getProductDetail().getImageByte()));
            }
        }

        return sellingOrderFullDto;
    }

    @Override
    public List<SellingOrderFullDto> getByCustomerId(String id) {
        List<SellingOrder> sellingOrders = sellingOrderRepository.getByCustomerId(id);
        List<SellingOrderFullDto> result = new ArrayList<>();

        for (SellingOrder sellingOrder : sellingOrders) {
            SellingOrderFullDto sellingOrderFullDto = getSellingOrderFullById(sellingOrder.getId());
            result.add(sellingOrderFullDto);
        }


        result.sort(Comparator.comparing(SellingOrderFullDto::getInvoiceDate).reversed());

        return result;
    }

    @Override
    public List<MonthRevenueDetailDto> getMonthRevenue(RangeDateDto rangeDateDto) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateNew = format1.format(rangeDateDto.getFromDate());
        String toDateNew = format1.format(rangeDateDto.getToDate());


        List<MonthRevenueDetail> monthRevenueDetails = sellingOrderDao.getMonthRevenue(fromDateNew, toDateNew);
        if (monthRevenueDetails == null) {
            return null;
        }

        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(new Date(rangeDateDto.getFromDate()));
        toCal.setTime(new Date(rangeDateDto.getToDate()));

        Integer fromMonth = fromCal.get(Calendar.MONTH) + 1;
        Integer toMonth = toCal.get(Calendar.MONTH) + 1;
        Integer fromYear = fromCal.get(Calendar.YEAR);
        Integer toYear = toCal.get(Calendar.YEAR);

        if (fromYear < toYear) {
            toMonth = 12;
        }
        List<MonthRevenueDetail> monthRevenueDetailList = new ArrayList<>();
        while (fromYear <= toYear) {
            if (fromYear - toYear == 0) {
                toMonth = toCal.get(Calendar.MONTH) + 1;
            }
            while (fromMonth <= toMonth) {
                MonthRevenueDetail monthRevenueDetail = new MonthRevenueDetail();
                monthRevenueDetail.setMonthDate(fromMonth);
                monthRevenueDetail.setYearDate(fromYear);
                monthRevenueDetail.setTotal(0);
                monthRevenueDetailList.add(monthRevenueDetail);
                fromMonth += 1;
            }
            fromMonth = 1;
            fromYear += 1;
        }
        for (MonthRevenueDetail monthRevenueDetail : monthRevenueDetails) {
            List<MonthRevenueDetail> detailLst = monthRevenueDetailList.stream().filter(item -> item.getMonthDate() == monthRevenueDetail.getMonthDate()).collect(Collectors.toList());
            for (MonthRevenueDetail temp : detailLst) {
                temp.setTotal(monthRevenueDetail.getTotal());
            }
        }
        List<MonthRevenueDetailDto> monthRevenueDtos = IMonthRevenueDetailDtoMapper.INSTANCE.toMonthRevenueDtoList(monthRevenueDetailList);

        return monthRevenueDtos;
    }

    @Override
    public List<DateRevenueDetailDto> getDateRevenue(RangeDateDto rangeDateDto) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateNew = format1.format(rangeDateDto.getFromDate());
        String toDateNew = format1.format(rangeDateDto.getToDate());

        List<DateRevenueDetail> dateRevenueDetails = sellingOrderDao.getDateRevenue(fromDateNew, toDateNew);
        if (dateRevenueDetails == null) {
            return null;
        }

        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(new Date(rangeDateDto.getFromDate()));
        toCal.setTime(new Date(rangeDateDto.getToDate()));

        Integer fromDate = fromCal.get(Calendar.DAY_OF_MONTH);
        Integer toDate = toCal.get(Calendar.DAY_OF_MONTH);
        Integer month = fromCal.get(Calendar.MONTH) + 1;
        Integer year = fromCal.get(Calendar.YEAR);

        List<DateRevenueDetail> dateRevenueDetailList = new ArrayList<>();
        while (fromDate <= toDate) {
            DateRevenueDetail dateRevenueDetail = new DateRevenueDetail();
            dateRevenueDetail.setDate(fromDate);
            dateRevenueDetail.setMonth(month);
            dateRevenueDetail.setYear(year);
            dateRevenueDetail.setTotal(0);
            dateRevenueDetailList.add(dateRevenueDetail);
            fromDate += 1;
        }

        for (DateRevenueDetail dateRevenueDetail : dateRevenueDetails) {
            List<DateRevenueDetail> detailLst = dateRevenueDetailList.stream().filter(item -> item.getDate() == dateRevenueDetail.getDate()).collect(Collectors.toList());
            for (DateRevenueDetail temp : detailLst) {
                temp.setTotal(dateRevenueDetail.getTotal());
            }
        }
        List<DateRevenueDetailDto> dateRevenueDetailDtos = IDateRevenueDetailDtoMapper.INSTANCE.toDateRevenueDtoList(dateRevenueDetailList);

        return dateRevenueDetailDtos;
    }

    @Override
    public List<YearRevenueDetailDto> getYearRevenue(RangeDateDto rangeDateDto) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateNew = format1.format(rangeDateDto.getFromDate());
        String toDateNew = format1.format(rangeDateDto.getToDate());

        List<YearRevenueDetail> yearRevenueDetails = sellingOrderDao.getYearRevenue(fromDateNew, toDateNew);
        if (yearRevenueDetails == null) {
            return null;
        }

        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(new Date(rangeDateDto.getFromDate()));
        toCal.setTime(new Date(rangeDateDto.getToDate()));

        Integer fromYear = fromCal.get(Calendar.YEAR);
        Integer toYear = toCal.get(Calendar.YEAR);

        List<YearRevenueDetail> yearRevenueDetailList = new ArrayList<>();
        while (fromYear <= toYear) {
            YearRevenueDetail yearRevenueDetail = new YearRevenueDetail();
            yearRevenueDetail.setYear(fromYear);
            yearRevenueDetail.setTotal(0);
            yearRevenueDetailList.add(yearRevenueDetail);
            fromYear += 1;
        }

        for (YearRevenueDetail yearRevenueDetail : yearRevenueDetails) {
            List<YearRevenueDetail> detailLst = yearRevenueDetailList.stream().filter(item -> item.getYear() == yearRevenueDetail.getYear()).collect(Collectors.toList());
            for (YearRevenueDetail temp : detailLst) {
                temp.setTotal(yearRevenueDetail.getTotal());
            }
        }
        List<YearRevenueDetailDto> yearRevenueDetailDtos = IYearRevenueDetailDtoMapper.INSTANCE.toYearRevenueDtoList(yearRevenueDetailList);

        return yearRevenueDetailDtos;
    }

}
