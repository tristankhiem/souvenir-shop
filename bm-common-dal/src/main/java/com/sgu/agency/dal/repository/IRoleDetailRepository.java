package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.RoleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleDetailRepository extends JpaRepository<RoleDetail, String> {
    @Query("select t from RoleDetail t where t.employee.id = ?1")
    List<RoleDetail> getDetailsByEmployeeId(String id);

    @Query("select t from RoleDetail t where t.role.agency.companyId = ?1 and t.role.name = 'Global Admin'")
    RoleDetail getGlobalAdminByCompany(String companyId);

    @Query("SELECT t FROM RoleDetail t WHERE t.employee.id = ?1 AND t.role.id = (SELECT r.id FROM Role r WHERE r.name = ?2 AND r.agency.id = ?3)")
    RoleDetail getEmployeeRoleByName(String employeeId, String roleName, String agencyId);
}
