package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IColorRepository extends JpaRepository<Color, String> {
    @Query("select t from Color t where t.name like %?1% ")
    List<Color> getLikeName(String colorName);

    @Query("select t from Color t where t.name = ?1 ")
    Color getByName(String colorName);
}
