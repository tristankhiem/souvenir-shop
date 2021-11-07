package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubCategoryRepository extends JpaRepository<SubCategory, String> {
}
