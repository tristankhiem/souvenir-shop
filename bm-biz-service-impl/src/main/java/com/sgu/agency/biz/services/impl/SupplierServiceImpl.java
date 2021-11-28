package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.dao.IEmployeeDao;
import com.sgu.agency.dal.entity.Customer;
import com.sgu.agency.dal.entity.Product;
import com.sgu.agency.dal.entity.Supplier;
import com.sgu.agency.dal.repository.ICustomerRepository;
import com.sgu.agency.dal.repository.ISupplierRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDto;
import com.sgu.agency.dtos.response.SupplierDto;
import com.sgu.agency.dtos.response.SupplierDto;
import com.sgu.agency.mappers.ICustomerDtoMapper;
import com.sgu.agency.mappers.IProductDtoMapper;
import com.sgu.agency.mappers.ISupplierDtoMapper;
import com.sgu.agency.services.ICustomerService;
import com.sgu.agency.services.ISupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SupplierServiceImpl implements ISupplierService {
    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    private ISupplierRepository supplierRepository;


    @Override
    @Transactional
    public List<SupplierDto> findAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ISupplierDtoMapper.INSTANCE.toSupplierDtoList(suppliers);
    }

    @Override
    public BaseSearchDto<List<SupplierDto>> findAll(BaseSearchDto<List<SupplierDto>> searchDto) {
        if (searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll());
            return searchDto;
        }
        Sort sort = null;
        if(searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<Supplier> page = supplierRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ISupplierDtoMapper.INSTANCE.toSupplierDtoList(page.getContent()));

        return searchDto;
    };
//
//    @Override
//    public CustomerDto getCustomerByEmailCompany(String email) {
//        Customer customer = customersRepository.getCustomerByEmailCompany(email);
//        return ICustomerDtoMapper.INSTANCE.toCustomerDto(customer);
//    }
//
    @Override
    @Transactional
    public SupplierDto insert(SupplierDto supplierDto) {
        try {
            Supplier supplier = ISupplierDtoMapper.INSTANCE.toSupplier(supplierDto);

            supplier.setId(UUIDHelper.generateType4UUID().toString());
            Supplier createdSupplier = supplierRepository.save(supplier);

            supplierDto.setId(createdSupplier.getId());
            return supplierDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public SupplierDto getSupplierById(String id) {
        try {
            Supplier supplier = supplierRepository.findById(id).get();
            SupplierDto supplierDto = ISupplierDtoMapper.INSTANCE.toSupplierDto(supplier);
            return supplierDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    };

    @Override
    @Transactional
    public SupplierDto update(SupplierDto supplierDto) {
        try {
            Supplier old = supplierRepository.findById(supplierDto.getId()).get();
            Supplier customer = ISupplierDtoMapper.INSTANCE.toSupplier(supplierDto);
            //customer.setPassword(old.getPassword());
            supplierRepository.save(customer);

            return supplierDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
    @Override
    @Transactional
    public boolean deleteEmployee(String id) {
        try {
            supplierRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    };
    @Override
    public List<SupplierDto> getLikeName(String searchName) {
        List<Supplier> suppliers = supplierRepository.getLikeName(searchName);
        return ISupplierDtoMapper.INSTANCE.toSupplierDtoList(suppliers);
    }
//    @Override
//    @Transactional
//    public CustomerDto changeAccountState(CustomerDto customerDto) {
//        try {
//            Customer old = customersRepository.findById(customerDto.getId()).get();
//            Customer customer = ICustomerDtoMapper.INSTANCE.toCustomer(customerDto);
//            customer.setPassword(old.getPassword());
//
//            if(old.getIsValid()==true)
//                customer.setIsValid(false);
//            else
//                customer.setIsValid(true);
//            customersRepository.save(customer);
//
//            return customerDto;
//        } catch (Exception ex) {
//            logger.error(ex.getMessage());
//            logger.error(ex.getStackTrace().toString());
//            return null;
//        }
//    }

}
