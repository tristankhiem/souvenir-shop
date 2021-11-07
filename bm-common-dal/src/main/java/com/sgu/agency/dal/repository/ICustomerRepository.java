package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Color;
import com.sgu.agency.dal.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, String> {
}
