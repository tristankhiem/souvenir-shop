package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.OrderStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SellingOrderFullDto {
    private String id;
    private CustomerDto customer;
    private String address;
    private String status;
    private Double total;
    private Date invoiceDate;
    private Date deliveryDate;
    private String receivePerson;
    private List<SellingTransactionDto> sellingTransactions;

    public void calculateTotal() {
        double total = 0d;
        for (SellingTransactionDto transactionDto : sellingTransactions) {
            total += transactionDto.getPrice() * transactionDto.getQuantity();
        }
        this.total = total;
    }
}
