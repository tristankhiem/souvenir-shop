package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ProductDetailDto {
    private String id;
    private String name;
    private Integer quantity;
    private Double sellingPrice;
    private Double importingPrice;
    private String imageUrl;
    private byte[] imageByte;
    private ColorDto color;
    private SizeDto size;
    private ProductDto product;
}
