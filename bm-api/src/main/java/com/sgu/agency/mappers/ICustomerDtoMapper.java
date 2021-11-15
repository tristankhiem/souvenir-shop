package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Customer;
import com.sgu.agency.dtos.response.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ICustomerDtoMapper {
    ICustomerDtoMapper INSTANCE = Mappers.getMapper( ICustomerDtoMapper.class );

    CustomerDto toCustomerDto(Customer customer);
    Customer toCustomer(CustomerDto customerDto);
    List<CustomerDto>toCustomersDtoList(List<Customer> customersList);


    //Customer toCustomer(CustomerDto categoryDto);


}
