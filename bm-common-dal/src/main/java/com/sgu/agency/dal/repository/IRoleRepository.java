package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role, String> {
    @Query("select r from Role r where r.agency.id = ?2 and r.name = ?1")
    Role getByName(String name, String agencyId);

    @Query("select r from Role r where r.agency.id = ?1")
    List<Role> findAllByAgencyId(String agencyId);

    @Query("select r from Role r where r.agency.id = ?1")
    Page<Role> findAllByAgencyId(Pageable var1, String agencyId);

    @Query("SELECT r FROM Role r WHERE r.agency.companyId = ?1")
    List<Role> findAllByCompanyId(String companyId);

    @Query("SELECT r FROM Role r WHERE r.agency.companyId = ?1")
    Page<Role> findAllByCompanyId(Pageable var1, String companyId);

    @Query("SELECT r FROM Role r WHERE r.agency.companyId = ?1 AND r.name <> 'Global Admin'")
    Page<Role> findNotGlobalAdminByCompanyId(Pageable var1, String companyId);

    @Query("select r from Role r where r.agency.id = ?1 and r.name <> 'Global Admin'")
    Page<Role> findNotGlobalAdminByAgencyId(Pageable var1, String agencyId);

    @Query("select t from Role t where t.name = ?1 and t.agency.id in ?2")
    List<Role> findAllByNameAndAgencyIdIn(String name, List<String> agencyId);

    @Query("select t from Role t where t.agency.companyId = ?1 and t.name = ?2")
    Role getRoleByNameAndCompany(String companyId, String name);
    
    @Query("SELECT t FROM Role t WHERE t.id IN (SELECT u.role.id FROM RoleDetail u WHERE u.employee.id = ?1)")
    List<Role> getByEmployeeId(String employeeId);

    @Query("SELECT t.id FROM Role t WHERE t.id IN (SELECT u.role.id FROM RoleDetail u WHERE u.employee.id = ?1)")
    List<String> getRolesIdByEmployeeId(String employeeId);

    @Query("SELECT t FROM Role t WHERE t.name = ?1 AND t.id IN (SELECT u.role.id FROM RoleDetail u WHERE u.employee.id = ?2)")
    Role getByNameAndEmployeeId(String roleName, String employeeId);
}
