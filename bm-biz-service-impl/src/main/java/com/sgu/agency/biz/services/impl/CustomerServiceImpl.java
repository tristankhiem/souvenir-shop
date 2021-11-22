package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.dao.IEmployeeDao;
import com.sgu.agency.dal.entity.Customer;
import com.sgu.agency.dal.entity.Employees;
import com.sgu.agency.dal.repository.ICustomerRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.CustomerDto;
import com.sgu.agency.dtos.response.EmployeesDto;
import com.sgu.agency.mappers.ICustomerDtoMapper;
import com.sgu.agency.mappers.IEmployeesDtoMapper;
import com.sgu.agency.services.ICustomerService;
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
public class CustomerServiceImpl implements ICustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private ICustomerRepository customersRepository;

    @Autowired
    private IEmployeeDao customerDao;



    @Override
    @Transactional
    public List<CustomerDto> findAll() {
        List<Customer> customers = customersRepository.findAll();
        return ICustomerDtoMapper.INSTANCE.toCustomersDtoList(customers);
    }

    @Override
    public BaseSearchDto<List<CustomerDto>> findAll(BaseSearchDto<List<CustomerDto>> searchDto) {
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

        Page<Customer> page = customersRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ICustomerDtoMapper.INSTANCE.toCustomersDtoList(page.getContent()));

        return searchDto;
    };

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customersRepository.getCustomerByEmail(email);
        return ICustomerDtoMapper.INSTANCE.toCustomerDto(customer);
    }

    @Override
    @Transactional
    public CustomerDto insert(CustomerDto customerDto) {
        try {
            Customer customer = ICustomerDtoMapper.INSTANCE.toCustomer(customerDto);

            customer.setId(UUIDHelper.generateType4UUID().toString());
            Customer createdCustomer = customersRepository.save(customer);

            customerDto.setId(createdCustomer.getId());
            return customerDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public CustomerDto getCustomerById(String id) {
        try {
            Customer customer = customersRepository.findById(id).get();
            CustomerDto customerDto = ICustomerDtoMapper.INSTANCE.toCustomerDto(customer);
            return customerDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    };

    @Override
    @Transactional
    public CustomerDto update(CustomerDto customerDto) {
        try {
            Customer old = customersRepository.findById(customerDto.getId()).get();
            Customer customer = ICustomerDtoMapper.INSTANCE.toCustomer(customerDto);
            customer.setPassword(old.getPassword());
            customersRepository.save(customer);

            return customerDto;
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
            customersRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    };
    @Override
    @Transactional
    public CustomerDto changeAccountState(CustomerDto customerDto) {
        try {
            Customer old = customersRepository.findById(customerDto.getId()).get();
            Customer customer = ICustomerDtoMapper.INSTANCE.toCustomer(customerDto);
            customer.setPassword(old.getPassword());

            if(old.getIsValid()==true)
                customer.setIsValid(false);
            else
                customer.setIsValid(true);
            customersRepository.save(customer);

            return customerDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

}
