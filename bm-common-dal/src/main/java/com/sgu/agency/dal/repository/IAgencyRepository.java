package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAgencyRepository extends JpaRepository<Agency, String> {
    @Query("select a from Agency a where a.companyId = ?1")
    List<Agency> getByCompanyId(String companyId);

    @Query("select u from Agency u where u.companyId = ?1")
    List<Agency> findAll(String companyId);

    @Query("select u from Agency u where u.companyId = ?1")
    Page<Agency> findAll(Pageable var1, String companyId);

    @Query("select u from Agency u where u.name like %?1% and u.companyId = ?2")
    List<Agency> getByName(String agencyName, String companyId);

    @Query("select u from Agency u where u.name = ?1 and u.companyId = ?2")
    Agency getByExactName(String agencyName, String companyId);

    @Query("select u from Agency u where (u.name like %?1% or u.orgCode like %?1%) and u.companyId = ?2")
    List<Agency> getLikeCodeOrName(String name, String companyId);

    @Query("select u from Agency u where (u.name like %?1% or u.orgCode like %?1%) and u.id <> ?2 and u.companyId = ?3")
    List<Agency> getDifferentByOrgCodeLike(String orgCode, String agencyId, String companyId);

    @Query("select u from Agency u where u.orgCode = ?1 and u.companyId = ?2")
    Agency getByOrgCode(String orgCode, String companyId);

    @Query("select count(u) from Agency u where u.companyId = ?1")
    int countAgencyByCompanyId(String companyId);

    @Query("SELECT a FROM Agency a WHERE a.phone = ?1")
    Agency getByPhone(String phone, String companyId);

    @Query(countQuery = "select t.id from Agency t where t.id = :agencyId and t.companyId = :companyId")
    int countByIdAndCompanyId(@Param("agencyId") String agencyId, @Param("companyId") String companyId);

    // Get all agency id of company by one agencyId
    @Query("SELECT a.id FROM Agency a WHERE a.companyId = (SELECT g.companyId FROM Agency g WHERE g.id = ?1)")
    List<String> getAllAgenciesByAgencyId(String agencyId);
}
