package com.sgu.agency.services;

import com.sgu.agency.dal.entity.Supplier;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.ChangePasswordDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.EmployeesDto;
import com.sgu.agency.dtos.response.RangeDateDto;
import com.sgu.agency.dtos.response.SupplierDto;

import java.util.List;

public interface ISupplierService {
    BaseSearchDto<List<SupplierDto>> findAll(BaseSearchDto<List<SupplierDto>> searchDto);
    List<SupplierDto> findAll();
    SupplierDto getSupplierById(String id);
//    EmployeesDto getEmployeeByEmail(String email);
//    EmployeesDto getEmployeeByEmailCompany(String email);
    List<SupplierDto> getLikeName(String name);
//    List<EmployeesDto> getEmployees(List<String> ids);
      SupplierDto insert(SupplierDto supplierDto);
      SupplierDto update(SupplierDto supplierDto);
      boolean deleteEmployee(String id);
//    EmployeeFullDto getEmployeeFullById(String id);
//    EmployeeFullDto getEmployeeFull(String email);
//    EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId);
//    Double getBonusByRangeDate(RangeDateDto rangeDateDto, String employeeId);
//    EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId);
//
//    String resetPassword(String employeeId);
}
