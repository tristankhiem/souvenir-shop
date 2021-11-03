package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.enums.*;
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
    public EmployeesDto getEmployeeByEmail(String email, String agencyId) {
        Employees employee = employeesRepository.getEmployeeByEmail(email, agencyId);
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
            employees.setBlockedStatus(BlockStatusEnum.APPROVED);
            Employees createdEmployee = employeesRepository.save(employees);

            for(RoleDetailFullDto detail : employeeFullDto.getRoleDetails()) {
                if (detail.getRole() == null || detail.getRole().getId() == null
                        || detail.getRole().getId().isEmpty()) {
                    continue;
                }

                RoleDetail tempDetail = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFull(detail);
                tempDetail.setId(UUIDHelper.generateType4UUID().toString());
                tempDetail.setEmployee(createdEmployee);

                roleDetailRepository.save(tempDetail);
            }

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
            for(RoleDetail item : roleDetails) {
                if (item.getId() == null || item.getId().isEmpty()) {
                    continue;
                }

                int index = employeeFullDto.getRoleDetails().stream().map(t -> t.getId()).collect(Collectors.toList()).indexOf(item.getId());
                int isExist = detailDelete.indexOf(item.getId());
                if (index == -1 && isExist == -1) {
                    detailDelete.add(item.getId());
                }
            }

            for(String id : detailDelete) {
                roleDetailRepository.deleteById(id);
            }

            for(RoleDetailFullDto roleDetailDto : employeeFullDto.getRoleDetails()) {
                if (roleDetailDto.getId() == null || roleDetailDto.getId().isEmpty()) {
                    EmployeesDto employeesDto = new EmployeesDto();
                    employeesDto.setId(employeeFullDto.getId());

                    roleDetailDto.setId(UUIDHelper.generateType4UUID().toString());
                    roleDetailDto.setEmployee(employeesDto);
                }
                RoleDetail roleDetail = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFull(roleDetailDto);
                roleDetail = roleDetailRepository.save(roleDetail);
                roleDetailDto = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFullDto(roleDetail);
            }

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
            for(RoleDetailFullDto detailDto : employeeFull.getRoleDetails()) {
                roleDetailRepository.deleteById(detailDto.getId());
            }
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
            employeeDto.setRoleDetails(detailDto);
            return employeeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
    @Override
    @Transactional
    public EmployeeFullDto getEmployeeFull(String email, String companyId) {
        Employees employee = employeesRepository.getEmployee(email, companyId);
        EmployeeFullDto employeeFullDto =IEmployeesDtoMapper.INSTANCE.toEmployeeFullDto(employee);
        if (employeeFullDto == null) {
            return employeeFullDto;
        }
        employeeFullDto = getRoleDetails(employeeFullDto);

        return employeeFullDto;
    }

    private EmployeeFullDto getRoleDetails(EmployeeFullDto employeeFullDto) {
        employeeFullDto.setRoleDetails(new ArrayList<>());
        List<RoleDetail> roleDetails = roleDetailRepository.getDetailsByEmployeeId(employeeFullDto.getId());
        if(roleDetails != null && roleDetails.size() > 0) {
            for (RoleDetail item : roleDetails) {
                RoleDetailFullDto roleDetailDto = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFullDto(item);
                List<GrantPermission> grantPermissions = grantPermissionRepository.getRoleId(item.getRole().getId());
                if (grantPermissions == null || grantPermissions.size() == 0) {
                    continue;
                }

                roleDetailDto.setRole(IRoleDtoMapper.INSTANCE.toRoleFullDto(grantPermissions.get(0).getRole()));
                roleDetailDto.getRole().setGrantPermissions((new ArrayList<>()));
                for (GrantPermission grantPermission : grantPermissions) {
                    roleDetailDto.getRole().getGrantPermissions().add(IGrantPermissionDtoMapper.INSTANCE.toGrantPermissionDto(grantPermission));
                }
                employeeFullDto.getRoleDetails().add(roleDetailDto);
            }
        }
        return employeeFullDto;
    }

    @Override
    @Transactional
    public EmployeesDto changePassword(String userName, ChangePasswordDto changePasswordDto, String agencyId) {
        try {
            // get Employee by userName
            Employees employees = employeesRepository.getEmployeeByEmail(userName, agencyId);
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
    @Transactional(rollbackOn = TransactionException.class)
    public boolean upgradeAdminCompany(List<String> features, String companyId) {
        RoleDetail roleDetail = roleDetailRepository.getGlobalAdminByCompany(companyId);
        Role roleGlobalAdmin = roleDetail.getRole();
        RoleDto roleGlobalAdminDto = IRoleDtoMapper.INSTANCE.toRoleDto(roleGlobalAdmin);

        // get current permission list
        List<GrantPermission> currentGrantPermissionList = grantPermissionRepository.getRoleId(roleGlobalAdmin.getId());
        List<String> currentPermissionCodeList = currentGrantPermissionList.stream()
                .map(s -> s.getPermission().getCode()).collect(Collectors.toList());

        // get new permission list
        List<Permission> newPermissionList = permissionRepository.getByFeatureKeyList(features);
        List<String> newPermissionCodeList = newPermissionList.stream()
                .map(Permission::getCode).collect(Collectors.toList());;

        // get new and deleted permission code
        List<String> insertPermissionCodeList = newPermissionCodeList.stream()
                .filter(p -> !currentPermissionCodeList.contains(p)).collect(Collectors.toList());
        List<String> deletePermissionCodeList = currentPermissionCodeList.stream()
                .filter(p -> !newPermissionCodeList.contains(p)).collect(Collectors.toList());

        // delete grant permission contain deleted code
        List<Role> rolesGetByCompanyId = roleRepository.findAllByCompanyId(companyId);
        List<String> roleIdsGetByCompanyId = rolesGetByCompanyId.stream().map(Role::getId).collect(Collectors.toList());
        if (deletePermissionCodeList.size() > 0) {
            grantPermissionRepository.deleteByPermissionListAndRoleList(deletePermissionCodeList, roleIdsGetByCompanyId);
        }

        List<GrantPermissionDto> insertGrantPermissionDtoList = new ArrayList<>();
        for (String p : insertPermissionCodeList) {
            GrantPermissionDto g = new GrantPermissionDto();
            g.setId(UUIDHelper.generateType4UUID().toString());
            g.setRole(roleGlobalAdminDto);
            PermissionDto permissionDto = new PermissionDto();
            permissionDto.setCode(p);
            g.setPermission(permissionDto);
            insertGrantPermissionDtoList.add(g);
        }
        List<GrantPermission> insertGrantPermissionList = grantPermissionRepository
                .saveAll(IGrantPermissionDtoMapper.INSTANCE.toGrantPermissionList(insertGrantPermissionDtoList));

        return true;
    }

    @Override
    public Integer countEmployeeByCompany(String companyId) {
        return employeesRepository.countEmployeeByCompanyId(companyId);
    }

    @Override
    public EmployeeFullDto getEmployeeFullByEmail(String email, String agencyId) {
        try {
            Employees employee = employeesRepository.getEmployeeByEmail(email, agencyId);
            List<RoleDetail> details = roleDetailRepository.getDetailsByEmployeeId(employee.getId());
            EmployeeFullDto employeeDto = IEmployeesDtoMapper.INSTANCE.toEmployeeFullDto(employee);
            List<RoleDetailFullDto> detailDto = IRoleDetailDtoMapper.INSTANCE.toRoleDetailFullListDto(details);
            employeeDto.setRoleDetails(detailDto);
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
