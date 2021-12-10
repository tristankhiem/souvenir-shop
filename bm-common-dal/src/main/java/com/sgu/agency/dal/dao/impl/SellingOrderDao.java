package com.sgu.agency.dal.dao.impl;

import com.sgu.agency.dal.dao.ICustomerDao;
import com.sgu.agency.dal.dao.ISellingOrderDao;
import com.sgu.agency.dal.data.DateRevenueDetail;
import com.sgu.agency.dal.data.MonthRevenueDetail;
import com.sgu.agency.dal.data.SearchResult;
import com.sgu.agency.dal.data.YearRevenueDetail;
import com.sgu.agency.dal.entity.Customer;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SellingOrderDao extends GenericDao implements ISellingOrderDao {
    @Override
    @Transactional
    public List<MonthRevenueDetail> getMonthRevenue(String fromDate, String toDate) {
        Session session = getSession();

        String queryStr = "select month(a.invoice_date) as monthDate, year(a.invoice_date) as yearDate, ifnull(sum(a.total), 0) " +
                "from selling_order a where a.invoice_date <= :toDate " + "and a.invoice_date >= :fromDate " +
                "group by year(a.invoice_date), month(a.invoice_date)";
        Query<Object[]> query = session.createNativeQuery(queryStr);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        List<MonthRevenueDetail> result = new ArrayList<>();
        List<Object[]> resultRows = query.getResultList();
        for (Object[] row : resultRows) {
            MonthRevenueDetail monthRevenueDetail = new MonthRevenueDetail();
            monthRevenueDetail.setMonthDate(((Integer) row[0]));
            monthRevenueDetail.setYearDate(((Integer) row[1]));
            monthRevenueDetail.setTotal(((Double) row[2]));
            result.add(monthRevenueDetail);
        }
        return result;
    }

    @Override
    @Transactional
    public List<DateRevenueDetail> getDateRevenue(String fromDate, String toDate) {
        Session session = getSession();

        String queryStr = "select day(a.invoice_date) as d, month(a.invoice_date) as monthDate, year(a.invoice_date) as yearDate, ifnull(sum(a.total), 0) " +
                "from selling_order a where a.invoice_date <= :toDate " + "and a.invoice_date >= :fromDate " +
                "group by day(a.invoice_date), year(a.invoice_date), month(a.invoice_date)";
        Query<Object[]> query = session.createNativeQuery(queryStr);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        List<DateRevenueDetail> result = new ArrayList<>();
        List<Object[]> resultRows = query.getResultList();
        for (Object[] row : resultRows) {
            DateRevenueDetail dateRevenueDetail = new DateRevenueDetail();
            dateRevenueDetail.setDate(((Integer) row[0]));
            dateRevenueDetail.setMonth(((Integer) row[1]));
            dateRevenueDetail.setYear(((Integer) row[2]));
            dateRevenueDetail.setTotal(((Double) row[3]).floatValue());
            result.add(dateRevenueDetail);
        }
        return result;
    }

    @Override
    @Transactional
    public List<YearRevenueDetail> getYearRevenue(String fromDate, String toDate) {
        Session session = getSession();

        String queryStr = "select year(a.invoice_date) as yearDate, ifnull(sum(a.total), 0) " +
                "from selling_order a where a.invoice_date <= :toDate " + "and a.invoice_date >= :fromDate " +
                "group by year(a.invoice_date)";
        Query<Object[]> query = session.createNativeQuery(queryStr);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        List<YearRevenueDetail> result = new ArrayList<>();
        List<Object[]> resultRows = query.getResultList();
        for (Object[] row : resultRows) {
            YearRevenueDetail yearRevenueDetail = new YearRevenueDetail();
            yearRevenueDetail.setYear(((Integer) row[0]));
            yearRevenueDetail.setTotal(((Double) row[1]).floatValue());
            result.add(yearRevenueDetail);
        }
        return result;
    }
}
