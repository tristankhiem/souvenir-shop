package com.sgu.agency.dal.dao.impl;

import com.sgu.agency.dal.dao.IEmployeeDao;
import com.sgu.agency.dal.data.SearchResult;
import com.sgu.agency.dal.entity.Employees;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeDao extends GenericDao implements IEmployeeDao {

    @Override
    @Transactional
    public SearchResult<List<Employees>> search(Map<String, Object> search, List<String> allAgencies) {
        SearchResult<List<Employees>> searchResult = new SearchResult<>();
        Session session = getSession();
        String employeeQueryStr = "select e.* from employees e ";
        String countTotalRecords = "select count(*) from ";

        List<String> conditions = new ArrayList<>();

        boolean searchAllAgency = search.get("searchAllAgency") != null && search.get("searchAllAgency").equals(true);
        if (searchAllAgency) {
            conditions.add("e.agency_id IN :allAgencies");
            search.put("allAgencies", allAgencies);
        } else {
            String agencyId = search.get("agencyId") != null ? (String) search.get("agencyId") : null;
            if (agencyId != null && !agencyId.isEmpty()) {
                conditions.add("e.agency_id = :agencyId");
            }
        }

        String fullName = search.get("full_name") != null ? (String) search.get("full_name") : null;
        if (fullName != null && !fullName.isEmpty()) {
            conditions.add("e.full_name LIKE :fullName");
            search.put("full_name", "%" + fullName + "%");
        }

        String email = search.get("email") != null ? (String) search.get("email") : null;
        if (email != null && !email.isEmpty()) {
            conditions.add("e.email LIKE :email");
            search.put("email", "%" + email + "%");
        }

        String paymentStatus = search.get("paymentStatus") != null ? (String) search.get("paymentStatus") : null;
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            if (paymentStatus.equals("COMPLETED")) {
                conditions.add("NOT EXISTS( SELECT r.* FROM referral_bonus r WHERE r.payment_status <> 'COMPLETED' AND r.employee_ref_id = e.id)" +
                        "AND NOT EXISTS( SELECT s.* FROM selling_bonus s WHERE s.payment_status <> 'COMPLETED' AND s.employee_id = e.id)");
            } else if (paymentStatus.equals("UNCOMPLETED")) {
                conditions.add("EXISTS( SELECT r.* FROM referral_bonus r WHERE r.payment_status <> 'COMPLETED' AND r.employee_ref_id = e.id)" +
                        "OR EXISTS( SELECT s.* FROM selling_bonus s WHERE s.payment_status <> 'COMPLETED' AND s.employee_id = e.id)");
            }
        }

        String employeeWhereStr = conditions.size() > 0 ? "where " + String.join(" and ", conditions) : "";
        employeeQueryStr = employeeQueryStr + " " + employeeWhereStr;
        Query<Employees> query = session.createNativeQuery(employeeQueryStr, Employees.class);
        Query countQuery = session.createSQLQuery(countTotalRecords + "(" + employeeQueryStr + " ) as r");
        query.setProperties(search);
        countQuery.setProperties(search);

        int start = search.get("currentPage") != null ? (int) search.get("currentPage") : 0;
        int size = search.get("recordOfPage") != null ? (int) search.get("recordOfPage") : 0;
        if (start >= 0 && size > 0) {
            query.setFirstResult(start * size);
            query.setMaxResults(size);
        }

        List<Employees> resultRows = query.getResultList();
        List<Employees> result = new ArrayList<>(resultRows);

        long totalRecord = Long.parseLong((countQuery.uniqueResult()).toString());
        searchResult.setResult(result);
        searchResult.setTotalRecords(totalRecord);

        return searchResult;
    }
}
