package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductFullDto {
    private String id;
    private String name;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private byte[] imageByte;
    private Double sellingPrice;
    private SubCategoryDto subCategory;
    private List<ProductDetailDto> productDetails;

}
