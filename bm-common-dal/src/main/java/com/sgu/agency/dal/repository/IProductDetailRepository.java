package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query("SELECT p FROM ProductDetail p where p.name LIKE %?1%")
    List<ProductDetail> getLikeName(String name);

    @Query("SELECT p FROM ProductDetail p where p.name = ?1")
    List<ProductDetail> getListById(String id);

    @Query("select p from ProductDetail p where p.name = ?1")
    ProductDetail getByName(String name);
}
