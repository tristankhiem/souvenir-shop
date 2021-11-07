package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.OrderStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ImportingOrderDto {
    private String id;
    private SupplierDto supplier;
    private EmployeesDto employee;
    private OrderStatusEnum status;
    private Double total;
    private Date invoiceDate;
    private Date deliveryDate;
}
