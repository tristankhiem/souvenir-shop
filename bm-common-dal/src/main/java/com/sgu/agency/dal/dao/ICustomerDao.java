package com.sgu.agency.dal.dao;

import com.sgu.agency.dal.data.SearchResult;
import com.sgu.agency.dal.entity.Customer;
import com.sgu.agency.dal.entity.Employees;

import java.util.List;
import java.util.Map;

public interface ICustomerDao {
    SearchResult<List<Customer>> search(Map<String, Object> search, List<String> allAgencies);
}
