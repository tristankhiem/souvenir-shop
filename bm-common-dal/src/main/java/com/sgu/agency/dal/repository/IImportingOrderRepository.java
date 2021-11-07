package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.ImportingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImportingOrderRepository extends JpaRepository<ImportingOrder, String> {
}
