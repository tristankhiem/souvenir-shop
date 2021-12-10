package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SellingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellingTransactionRepository extends JpaRepository<SellingTransaction, String> {
    @Query("SELECT i FROM SellingTransaction i where i.sellingOrder.id = ?1")
    List<SellingTransaction> getListBySellingOrderId(String id);

}
