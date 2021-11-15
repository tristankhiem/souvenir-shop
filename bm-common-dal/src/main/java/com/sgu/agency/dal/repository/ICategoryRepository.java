package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Category;
import com.sgu.agency.dal.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, String> {
    @Query("select t from Category t where t.name like %?1% ")
    List<Category> getLikeName(String categoryName);

    @Query("select t from Category t where t.name = ?1 ")
    Category getByName(String categoryName);
}
