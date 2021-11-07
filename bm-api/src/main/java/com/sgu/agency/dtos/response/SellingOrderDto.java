package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.OrderStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class SellingOrderDto {
    private String id;
    private CustomerDto customer;
    private String address;
    private OrderStatusEnum status;
    private Double total;
    private Date invoiceDate;
    private Date deliveryDate;
}
