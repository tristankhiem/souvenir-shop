package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p where p.name LIKE %?1%")
    List<Product> getLikeName(String name);

    @Query("select p from Product p where p.name = ?1")
    Product getByName(String name);
}
