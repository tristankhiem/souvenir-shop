package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SubCategoryDto {
    private String id;
    @NotEmpty(message = "Tên của nhóm hàng hóa không được để trống")
    private String name;
    private CategoryDto category;
}
