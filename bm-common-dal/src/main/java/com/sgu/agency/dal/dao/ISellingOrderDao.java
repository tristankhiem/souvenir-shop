package com.sgu.agency.dal.dao;

import com.sgu.agency.dal.data.DateRevenueDetail;
import com.sgu.agency.dal.data.MonthRevenueDetail;
import com.sgu.agency.dal.data.SearchResult;
import com.sgu.agency.dal.data.YearRevenueDetail;
import com.sgu.agency.dal.entity.Customer;

import java.util.List;
import java.util.Map;

public interface ISellingOrderDao {
    List<MonthRevenueDetail> getMonthRevenue(String fromDate, String toDate);
    List<DateRevenueDetail> getDateRevenue(String fromDate, String toDate);
    List<YearRevenueDetail> getYearRevenue(String fromDate, String toDate);
}
