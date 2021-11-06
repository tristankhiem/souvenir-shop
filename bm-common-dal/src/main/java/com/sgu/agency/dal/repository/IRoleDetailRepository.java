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

    @Query("SELECT t FROM RoleDetail t WHERE t.employee.id = ?1 AND t.role.id = (SELECT r.id FROM Role r WHERE r.name = ?2)")
    RoleDetail getEmployeeRoleByName(String employeeId, String roleName, String agencyId);
}
