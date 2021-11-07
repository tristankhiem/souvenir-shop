package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class ProductDto {
    private String id;
    private String name;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Double sellingPrice;
    private SubCategoryDto subCategory;
}
