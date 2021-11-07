package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISizeRepository extends JpaRepository<Size, String> {
}
