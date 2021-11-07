package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, String> {
}
