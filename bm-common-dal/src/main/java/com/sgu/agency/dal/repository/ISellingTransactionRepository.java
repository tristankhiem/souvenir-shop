package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SellingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellingTransactionRepository extends JpaRepository<SellingTransaction, String> {
}
