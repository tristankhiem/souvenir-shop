package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.ChangePasswordDto;
import com.sgu.agency.dtos.response.*;

import java.util.List;

public interface IEmployeeService {
    BaseSearchDto<List<EmployeesDto>> findAll(BaseSearchDto<List<EmployeesDto>> searchDto);
    List<EmployeesDto> findAll();
    EmployeesDto getEmployeeById(String id);
    EmployeesDto getEmployeeByEmail(String email);
    EmployeesDto getEmployeeByEmailCompany(String email);
    List<EmployeesDto> getLikeName(String employeeName, String agencyId);
    List<EmployeesDto> getEmployees(List<String> ids);
    EmployeesDto insert(EmployeesDto employeesDto);
    EmployeesDto update(EmployeesDto employeesDto);
    boolean deleteEmployee(String id);
    EmployeeFullDto getEmployeeFullById(String id);
    EmployeeFullDto getEmployeeFull(String email);
    EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId);
    Double getBonusByRangeDate(RangeDateDto rangeDateDto, String employeeId);
    EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId);

    String resetPassword(String employeeId);
}
