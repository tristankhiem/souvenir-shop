package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Employees;
import com.sgu.agency.dtos.response.EmployeeDetailDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.EmployeesDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IEmployeesDtoMapper {
    IEmployeesDtoMapper INSTANCE = Mappers.getMapper( IEmployeesDtoMapper.class );

    EmployeesDto toEmployeesDto(Employees employee);

    Employees toEmployees(EmployeeFullDto employeeFullDto);
    EmployeeFullDto toEmployeeFullDto(Employees employees);
    EmployeeDetailDto toEmployeeDetailDto(EmployeesDto employeesDto);
    List<EmployeesDto> toEmployeesDtoList(List<Employees> employeeList);
    List<EmployeeDetailDto> toEmployeeDetailDtos(List<EmployeesDto> employeesDtos);
    List<EmployeeDetailDto> toEmployeeDetailDtoList(List<Employees> employeesList);
}
