package com.sgu.agency.dtos.response;

import com.sgu.agency.mappers.ICategoryFullDtoMapper;
import lombok.Data;

import java.util.List;

@Data
public class CategoryFullDto extends CategoryDto {
    private List<SubCategoryDto> subCategoryDtoList;
}
