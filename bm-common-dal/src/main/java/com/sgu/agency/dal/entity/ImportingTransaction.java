package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "importing_transaction")
public class ImportingTransaction {
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
    @JoinColumn(name = "importing_order_id")
    private ImportingOrder importingOrder;
}
