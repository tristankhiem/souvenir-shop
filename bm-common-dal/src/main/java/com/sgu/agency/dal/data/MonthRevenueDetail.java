package com.sgu.agency.dal.data;

import lombok.Data;

@Data
public class MonthRevenueDetail {
    private int monthDate;
    private int yearDate;
    private double total;
}
