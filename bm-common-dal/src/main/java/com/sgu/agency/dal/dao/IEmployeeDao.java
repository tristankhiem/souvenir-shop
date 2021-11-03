package com.sgu.agency.dal.dao;

import com.sgu.agency.dal.data.SearchResult;
import com.sgu.agency.dal.entity.Employees;

import java.util.List;
import java.util.Map;

public interface IEmployeeDao {
    SearchResult<List<Employees>> search(Map<String, Object> search, List<String> allAgencies);
}
