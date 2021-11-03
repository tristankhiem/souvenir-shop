package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeesRepository extends JpaRepository<Employees, String> {
    @Query("select u from Employees u where u.agency.companyId = ?2 and u.email = ?1")
    Employees getEmployee(String email, String companyId);

    @Query("select u from Employees u where u.agency.id = ?2 and u.email = ?1")
    Employees getEmployeeByEmail(String email, String agencyId);

    @Query("SELECT e FROM Employees e WHERE e.agency.companyId = ?2 AND e.email = ?1")
    Employees getEmployeeByEmailCompany(String email, String companyId);

    @Query("select t from Employees t where t.agency.id = ?2 and t.fullName like %?1% ")
    List<Employees> getLikeName(String employeeName, String agencyId);

    @Query("select u from Employees u where u.id in (?1) ")
    List<Employees> getEmployeesById(List<String> ids);

    @Query("SELECT e FROM Employees e WHERE e.agency IN (?1)")
    List<Employees> getEmployeeListByAgencyList(List<String> agencyIds);

    @Query(value= "select count(u.id) from employees u where u.id = ?1" , nativeQuery=true)
    int countEmployeeId(String employeeId);

    @Query(value= "select * from employees u where u.agency_id = ?1" , nativeQuery=true)
    List<Employees> findAllByAgencyId(String agencyId);

    @Query(value= "select * from employees u where u.agency_id = ?1" , nativeQuery=true)
    Page<Employees> findAllByAgencyId(Pageable var1, String agencyId);

    @Query(value= "select * from employees u where u.agency_id = ?1 and u.email like %?2%" , nativeQuery=true)
    Page<Employees> findAllByAgencyIdLikeEmail(Pageable var1, String agencyId, String email);

    @Query(value = "SELECT e FROM Employees e WHERE e.agency.companyId = ?1")
    Page<Employees> findAllByCompanyId(Pageable var1, String companyId);

    @Query(value = "SELECT e FROM Employees e WHERE e.agency.companyId = ?1 AND e.email = ?2")
    Page<Employees> findAllByCompanyIdLikeEmail(Pageable var, String companyId, String email);

    @Query("select count(u) from Employees u where u.agency.companyId = ?1")
    int countEmployeeByCompanyId(String companyId);
}
