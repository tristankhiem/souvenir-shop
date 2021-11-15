package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubCategoryRepository extends JpaRepository<SubCategory, String> {
    @Query("select t from SubCategory t where t.name like %?1% ")
    List<SubCategory> getLikeName(String categoryName);

    @Query("select t from SubCategory t where t.name = ?1 ")
    SubCategory getByName(String categoryName);
}
