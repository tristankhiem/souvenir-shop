package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Category;
import com.sgu.agency.dtos.response.EmployeeDetailDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ICategoryDtoMapper {
    ICategoryDtoMapper INSTANCE = Mappers.getMapper( ICategoryDtoMapper.class );

    CategoryDto toCategoryDto(Category employee);
    Category toCategory(CategoryDto categoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categories);

}
