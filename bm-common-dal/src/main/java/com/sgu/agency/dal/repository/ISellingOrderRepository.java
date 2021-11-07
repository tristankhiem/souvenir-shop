package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SellingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellingOrderRepository extends JpaRepository<SellingOrder, String> {
}
