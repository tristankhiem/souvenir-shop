package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MonthCostDto {
    private int monthDate;
    private int yearDate;
    private double total;
}
