package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Color;
import com.sgu.agency.dal.entity.Customer;
import com.sgu.agency.dal.entity.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, String> {
    @Query("select u from Customer u where u.email = ?1")
    Customer getCustomer(String email);

    @Query("select u from Customer u where u.email = ?1")
    Customer getCustomerByEmail(String email);

    @Query("select t from Customer t where t.name like %?1% ")
    List<Customer> getLikeName(String customerName, String agencyId);

    @Query("select u from Customer u where u.id in (?1) ")
    List<Customer> getCustomerById(List<String> ids);

    @Query(value= "select * from Customer u where u.agency_id = ?1" , nativeQuery=true)
    List<Customer> findAllByAgencyId(String agencyId);

    @Query(value= "select * from Customer u where u.agency_id = ?1" , nativeQuery=true)
    Page<Customer> findAllByAgencyId(Pageable var1, String agencyId);

}
