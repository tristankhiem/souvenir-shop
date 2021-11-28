package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.ImportingTransaction;
import com.sgu.agency.dal.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImportingTransactionRepository extends JpaRepository<ImportingTransaction, String> {
    @Query("SELECT i FROM ImportingTransaction i where i.importingOrder.id = ?1")
    List<ImportingTransaction> getListByImportingOrderId(String id);
}
