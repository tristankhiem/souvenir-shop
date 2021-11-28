package com.sgu.agency.dal.entity;

import com.sgu.agency.common.enums.OrderStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "importing_order")
public class ImportingOrder {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;
    @Column
    private String status;
    @Column
    private Double total;
    @Column(name = "invoice_date")
    private Date invoiceDate;
    @Column(name = "delivery_date")
    private Date deliveryDate;
}
