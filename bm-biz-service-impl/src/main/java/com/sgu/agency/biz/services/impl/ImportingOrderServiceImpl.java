package com.sgu.agency.biz.services.impl;
import com.sgu.agency.common.utils.FileReaderUtil;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.dao.IEmployeeDao;
import com.sgu.agency.dal.entity.*;
import com.sgu.agency.dal.repository.*;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;
import com.sgu.agency.mappers.*;
import com.sgu.agency.services.IEmployeeService;
import com.sgu.agency.services.IImportingOrderService;
import com.sgu.agency.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportingOrderServiceImpl implements IImportingOrderService {
    private static final Logger logger = LoggerFactory.getLogger(ImportingOrderServiceImpl.class);

    @Autowired
    private IImportingOrderRepository importingOrderRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IImportingTransactionRepository importingTransactionRepository;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductDetailServiceImpl productDetailService;
    @Autowired
    private IProductDetailRepository productDetailRepository;

    private ProductDetailDto productDetailDto;
    private ProductDetail productDetail;

    @Override
    @Transactional
    public List<ImportingOrderDto> findAll() {
        List<ImportingOrder> importingOrders = importingOrderRepository.findAll();
        return IImportingOrderDtoMapper.INSTANCE.toImportingOrderDtos(importingOrders);
    }
    @Override
    public BaseSearchDto<List<ImportingOrderDto>> findAll(BaseSearchDto<List<ImportingOrderDto>> searchDto) {
        if(searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll());
            return searchDto;
        }

        Sort sort = null;
        if(searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<ImportingOrder> page = importingOrderRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(IImportingOrderDtoMapper.INSTANCE.toImportingOrderDtos(page.getContent()));

        return searchDto;
    }

    @Override
    public List<ImportingTransaction> getListImportingTransactionByImportingOrderId(String id) {
        List<ImportingTransaction> importingTransactions = importingTransactionRepository.getListByImportingOrderId(id);
        return importingTransactions;
    }
    @Override
    @Transactional
    public ImportingOrderFullDto insert(ImportingOrderFullDto importingOrderFullDto) {
        try {
            //importingOrder
            importingOrderFullDto.setId(UUIDHelper.generateType4UUID().toString());
            ImportingOrder importing = IImportingOrderDtoMapper.INSTANCE.toImportingOrder(importingOrderFullDto);
            //importingTransaction + save +update quantity
            List<ImportingTransaction> importingTransactions = IImportingTransactionDtoMapper
                    .INSTANCE.toImportingTransactions(importingOrderFullDto.getImportingTransactions());

            Double total=0.0;
            for (ImportingTransaction importingTransaction :importingTransactions)
            {
                importingTransaction.setId(UUIDHelper.generateType4UUID().toString());
                importingTransaction.setImportingOrder(importing);

                productDetailDto = productDetailService.getById(importingTransaction.getProductDetail().getId());
                productDetailDto.setQuantity(productDetailDto.getQuantity() + importingTransaction.getQuantity());
                 productDetail =IProductDetailDtoMapper.INSTANCE.toProductDetail(productDetailDto);

                 total= importingTransaction.getPrice() + total;
                productDetailRepository.save(productDetail);
                importingTransactionRepository.save(importingTransaction);
            }
            // save importing order +
            importing.setTotal(total);
            importingOrderRepository.save(importing);
            //  importingTransaction -> importingTransactionDTO
            importingOrderFullDto.setImportingTransactions(IImportingTransactionDtoMapper.
                    INSTANCE.toImportingTransactionDtos(importingTransactions));

            //return dto
            ImportingOrderFullDto createdImportingOrderFullDto = importingOrderFullDto;


            return createdImportingOrderFullDto;

        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            importingOrderRepository.deleteById(id);
//            importingTransactionRepository.deleteById();
//           ImportingOrder importingOrder = importingOrderRepository.getOne(id);
            List <ImportingTransaction> importingTransactions = getListImportingTransactionByImportingOrderId(id);
            importingTransactionRepository.deleteAll(importingTransactions);

            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }
    @Override
    public ImportingOrderFullDto getImportingOrderFullById(String id)
    {
        ImportingOrder importingOrder = importingOrderRepository.findById(id).get();
        List<ImportingTransaction> importingTransactions = importingTransactionRepository.getListByImportingOrderId(id);
        ImportingOrderFullDto importingOrderFullDto = IImportingOrderDtoMapper.INSTANCE.toImportingOrderFullDto(importingOrder);

        importingOrderFullDto.setImportingTransactions(IImportingTransactionDtoMapper.INSTANCE.toImportingTransactionDtos(importingTransactions));


        return importingOrderFullDto;
    }
}

// bo: ImportingOrder importingOrder = getImportingOrderById(importingOrderFullDto.getId());
// bo:  List<ImportingTransaction> importingTransactions = getListImportingTransactionByImportingOrderId(importingOrderFullDto.getId());
//     importingOrderRepository.save(tmpImportingOrder);
//         importingTransactionRepository.save(importingTransaction);


//                 productDto = productService.getById(importingTransaction.getProductDetail().getId());
//                productDto.setQuantity(productDto.getQuantity() + importingTransaction.getQuantity());
//                Product product=IProductDtoMapper.INSTANCE.toProduct(productDto);
//                productRepository.save(product);