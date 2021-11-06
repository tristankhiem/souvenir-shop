package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.common.utils.RandomTextHelper;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.dao.IEmployeeDao;
import com.sgu.agency.dal.entity.*;
import com.sgu.agency.dal.repository.*;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.ChangePasswordDto;
import com.sgu.agency.dtos.response.*;
import com.sgu.agency.mappers.*;
import com.sgu.agency.services.IEmployeeService;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private IEmployeesRepository employeesRepository;
    @Autowired
    private IRoleDetailRepository roleDetailRepository;
    @Autowired
    private IGrantPermissionRepository grantPermissionRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IPermissionRepository permissionRepository;
    @Autowired
    private IAgencyRepository agencyRepository;
    @Autowired
    private IEmployeeDao employeeDao;

    @Override
    @Transactional
    public List<EmployeesDto> findAll(String agencyId) {
        List<Employees> employees = employeesRepository.findAllByAgencyId(agencyId);
        return IEmployeesDtoMapper.INSTANCE.toEmployeesDtoList(employees);
    }

    @Override
    public EmployeesDto getEmployeeByEmail(String email) {
        Employees employee = employeesRepository.getEmployeeByEmail(email);
        return IEmployeesDtoMapper.INSTANCE.toEmployeesDto(employee);
    }

    @Override
    public EmployeesDto getEmployeeByEmailCompany(String email, String companyId) {
        Employees employee = employeesRepository.getEmployeeByEmailCompany(email, companyId);
        return IEmployeesDtoMapper.INSTANCE.toEmployeesDto(employee);
    }

    @Override
    public List<EmployeesDto> getEmployees(List<String> ids) {
        List<Employees> employees = employeesRepository.getEmployeesById(ids);
        return IEmployeesDtoMapper.INSTANCE.toEmployeesDtoList(employees);
    }

    @Override
    public List<EmployeesDto> getLikeName(String employeeName, String agencyId) {
        List<Employees> employee = employeesRepository.getLikeName(employeeName, agencyId);
        return IEmployeesDtoMapper.INSTANCE.toEmployeesDtoList(employee);
    }

    @Override
    @Transactional
    public EmployeeFullDto insert(EmployeeFullDto employeeFullDto) {
        try {
            Employees employees = IEmployeesDtoMapper.INSTANCE.toEmployees(employeeFullDto);

            employees.setId(UUIDHelper.generateType4UUID().toString());
            Employees createdEmployee = employeesRepository.save(employees);

            employeeFullDto.setId(createdEmployee.getId());
            return employeeFullDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public EmployeeFullDto update(EmployeeFullDto employeeFullDto) {
        try {
            Employees old = employeesRepository.findById(employeeFullDto.getId()).get();
            Employees employees = IEmployeesDtoMapper.INSTANCE.toEmployees(employeeFullDto);
            employees.setPassword(old.getPassword());
            employeesRepository.save(employees);
            // collect role detail was removed.
            List<RoleDetail> roleDetails = roleDetailRepository.getDetailsByEmployeeId(employeeFullDto.getId());
            List<String> detailDelete = new ArrayList<>();

            return employeeFullDto;
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
            EmployeeFullDto employeeFull = this.getEmployeeFullById(id);
            employeesRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public BaseSearchDto<List<EmployeesDto>> findAll(BaseSearchDto<List<EmployeesDto>> searchDto, String agencyId) {
        if(searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll(agencyId));
            return searchDto;
        }

        Sort sort = null;
        if(searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<Employees> page = employeesRepository.findAllByAgencyId(request, agencyId);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(IEmployeesDtoMapper.INSTANCE.toEmployeesDtoList(page.getContent()));

        return searchDto;
    }

    @Override
    public EmployeesDto getEmployeeById(String id) {
        try {
            Employees employee = employeesRepository.findById(id).get();
            EmployeesDto employeeDto = IEmployeesDtoMapper.INSTANCE.toEmployeesDto(employee);
            return employeeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public EmployeeFullDto getEmployeeFullById(String id) {
        try {
            Employees employee = employeesRepository.findById(id).get();
            List<RoleDetail> details = roleDetailRepository.getDetailsByEmployeeId(employee.getId());
            EmployeeFullDto employeeDto = IEmployeesDtoMapper.INSTANCE.toEmployeeFullDto(employee);
            List<RoleDetailFullDto> detailDto = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFullListDto(details);
            // employeeDto.setRoleDetails(detailDto);
            return employeeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
    @Override
    @Transactional
    public EmployeeFullDto getEmployeeFull(String email) {
        Employees employee = employeesRepository.getEmployee(email);
        EmployeeFullDto employeeFullDto = IEmployeesDtoMapper.INSTANCE.toEmployeeFullDto(employee);
        if (employeeFullDto == null) {
            return employeeFullDto;
        }
        List<GrantPermission> grantPermissions = grantPermissionRepository.getRoleId(employeeFullDto.getRole().getId());
        List<GrantPermissionDto> grantPermissionDtos = IGrantPermissionDtoMapper.INSTANCE.toGrantPermissionDtoList(grantPermissions);
        employeeFullDto.getRole().setGrantPermissions(grantPermissionDtos);
        return employeeFullDto;
    }

    @Override
    @Transactional
    public EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId) {
        try {
            // get Employee by userName
            Employees employees = employeesRepository.getEmployeeByEmail(userName);
            employees.setPassword(BCryptHelper.encode(changePasswordDto.getNewPassword()));

            Employees updatedEmployee = employeesRepository.save(employees);
            return IEmployeesDtoMapper.INSTANCE.toEmployeesDto(updatedEmployee);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public Double getBonusByRangeDate(RangeDateDto rangeDateDto, String employeeId) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = format1.format(rangeDateDto.getFromDate());
        String toDate = format1.format(rangeDateDto.getToDate());
        Double totalBonus = 0D;
        return totalBonus;
    }

    @Override
    public EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId) {
        try {
            Employees employee = employeesRepository.getEmployeeByEmail(email);
            List<RoleDetail> details = roleDetailRepository.getDetailsByEmployeeId(employee.getId());
            EmployeeFullDto employeeDto = IEmployeesDtoMapper.INSTANCE.toEmployeeFullDto(employee);
            List<RoleDetailFullDto> detailDto = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFullListDto(details);
            // employeeDto.setRoleDetails(detailDto);
            return employeeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public String resetPassword(String employeeId) {
        try {
            Employees employees = employeesRepository.getOne(employeeId);
            String newPassword = RandomTextHelper.generateRandomPassword(7);
            String cryptPassword = BCryptHelper.encode(newPassword);
            employees.setPassword(cryptPassword);
            Employees updatedEmployee = employeesRepository.save(employees);
            return newPassword;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
}
