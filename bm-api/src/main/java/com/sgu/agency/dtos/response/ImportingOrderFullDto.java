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

    public void calculateTotal() {
        double total = 0d;
        for (ImportingTransactionDto transactionDto : importingTransactions) {
            total += transactionDto.getPrice() * transactionDto.getQuantity();
        }
        this.total = total;
    }
}
