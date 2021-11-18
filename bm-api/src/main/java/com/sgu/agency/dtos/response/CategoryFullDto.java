package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryFullDto extends CategoryDto {
    private List<SubCategoryDto> subCategories;
}
