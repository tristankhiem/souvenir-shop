package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.ImportingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImportingTransactionRepository extends JpaRepository<ImportingTransaction, String> {
}
