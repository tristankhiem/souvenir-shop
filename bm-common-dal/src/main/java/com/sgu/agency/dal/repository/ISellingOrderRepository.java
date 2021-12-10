package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SellingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellingOrderRepository extends JpaRepository<SellingOrder, String> {
    @Query("select t from SellingOrder t where t.customer.id = ?1")
    List<SellingOrder> getByCustomerId(String customerId);
}
