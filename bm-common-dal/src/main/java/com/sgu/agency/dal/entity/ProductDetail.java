package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Integer quantity;
    @Column(name = "selling_price")
    private Double sellingPrice;
    @Column(name = "importing_price")
    private Double importingPrice;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
