package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.ChangePasswordDto;
import com.sgu.agency.dtos.response.CustomerDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.EmployeesDto;
import com.sgu.agency.dtos.response.RangeDateDto;

import java.util.List;

public interface ICustomerService {
    BaseSearchDto<List<CustomerDto>> findAll(BaseSearchDto<List<CustomerDto>> searchDto);
    List<CustomerDto> findAll();
    CustomerDto getCustomerById(String id);
    //CustomerDto getCustomerByEmail(String email);
    CustomerDto getCustomerByEmail(String email);
    //List<CustomerDto> getLikeName(String customerName, String agencyId);
    //List<CustomerDto> getCustomers(List<String> ids);
    CustomerDto insert(CustomerDto customerDto);
    CustomerDto update(CustomerDto customerDto);
    CustomerDto changeAccountState(CustomerDto customerDto);
    boolean deleteEmployee(String id);
    //EmployeeFullDto getEmployeeFullById(String id);
    //EmployeeFullDto getEmployeeFull(String email);
    //EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId);
    //Double getBonusByRangeDate(RangeDateDto rangeDateDto, String employeeId);
    //EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId);

    //String resetPassword(String customerId);
}
