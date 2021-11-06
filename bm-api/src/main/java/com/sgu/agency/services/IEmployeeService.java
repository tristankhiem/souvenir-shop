package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.ChangePasswordDto;
import com.sgu.agency.dtos.response.*;

import java.util.List;

public interface IEmployeeService {
    BaseSearchDto<List<EmployeesDto>> findAll(BaseSearchDto<List<EmployeesDto>> searchDto, String agencyId);
    List<EmployeesDto> findAll(String agencyId);
    EmployeesDto getEmployeeById(String id);
    EmployeesDto getEmployeeByEmail(String email);
    EmployeesDto getEmployeeByEmailCompany(String email, String companyId);
    List<EmployeesDto> getLikeName(String employeeName, String agencyId);
    List<EmployeesDto> getEmployees(List<String> ids);
    EmployeeFullDto insert(EmployeeFullDto employeeFullDto);
    EmployeeFullDto update(EmployeeFullDto employeeFullDto);
    boolean deleteEmployee(String id);
    EmployeeFullDto getEmployeeFullById(String id);
    EmployeeFullDto getEmployeeFull(String email);
    EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId);
    Double getBonusByRangeDate(RangeDateDto rangeDateDto, String employeeId);
    EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId);

    String resetPassword(String employeeId);
}
