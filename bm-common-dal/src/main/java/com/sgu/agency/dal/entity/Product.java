package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Integer quantity;
    @Column
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "image_byte", length = 1000)
    private byte[] imageByte;
    @Column(name = "selling_price")
    private Double sellingPrice;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
}
