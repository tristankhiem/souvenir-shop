package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "selling_transaction")
public class SellingTransaction {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    @Column
    private Integer quantity;
    @Column
    private Double price;
    @ManyToOne
    @JoinColumn(name = "selling_order_id")
    private SellingOrder sellingOrder;
}
