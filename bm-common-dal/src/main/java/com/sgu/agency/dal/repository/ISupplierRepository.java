package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Product;
import com.sgu.agency.dal.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, String> {
    @Query("SELECT s FROM Supplier s where s.name LIKE %?1%")
    List<Supplier> getLikeName(String name);
}
