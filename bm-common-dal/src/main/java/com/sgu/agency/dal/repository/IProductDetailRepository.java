package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDetailRepository extends JpaRepository<ProductDetail, String> {
}
