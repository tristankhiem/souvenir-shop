package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.Category;
import com.sgu.agency.dal.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColorRepository extends JpaRepository<Color, String> {
}
