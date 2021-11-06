package com.sgu.agency.dal.entity;

import com.sgu.agency.common.enums.OrderStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "selling_order")
public class SellingOrder {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column
    private String address;
    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatusEnum status;
    @Column
    private Double total;
    @Column(name = "invoice_date")
    private Date invoiceDate;
    @Column(name = "delivery_date")
    private Date deliveryDate;
}
