package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISizeRepository extends JpaRepository<Size, String> {
    @Query("select t from Size t where t.name like %?1% ")
    List<Size> getLikeName(String sizeName);

    @Query("select t from Size t where t.name = ?1 ")
    Size getByName(String sizeName);
}
