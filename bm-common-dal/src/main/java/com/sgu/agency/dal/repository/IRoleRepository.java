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
    @Query("select r from Role r where r.name = ?1")
    Role getByName(String name);

    @Query("select r from Role r")
    List<Role> findAllByAgencyId(String agencyId);

    @Query("select r from Role r")
    Page<Role> findAllByAgencyId(Pageable var1, String agencyId);

    @Query("SELECT r FROM Role r")
    List<Role> findAllByCompanyId(String companyId);

    @Query("SELECT r FROM Role r")
    Page<Role> findAllByCompanyId(Pageable var1, String companyId);

    @Query("SELECT r FROM Role r WHERE r.name <> 'Global Admin'")
    Page<Role> findNotGlobalAdminByCompanyId(Pageable var1, String companyId);

    @Query("select r from Role r where r.name <> 'Global Admin'")
    Page<Role> findNotGlobalAdminByAgencyId(Pageable var1, String agencyId);

    @Query("select t from Role t where t.name = ?2")
    Role getRoleByNameAndCompany(String companyId, String name);

    @Query("SELECT r FROM Role r where r.name LIKE %?1%")
    List<Role> getLikeName(String name);

}
