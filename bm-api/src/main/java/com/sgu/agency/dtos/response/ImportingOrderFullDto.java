package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ImportingOrderFullDto {
    private String id;
    private SupplierDto supplier;
    private EmployeesDto employee;
    private String status;
    private Double total;
    private Date invoiceDate;
    private Date deliveryDate;
    private List<ImportingTransactionDto> importingTransactions;
}
