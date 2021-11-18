package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Category;
import com.sgu.agency.dtos.response.CategoryDto;
import com.sgu.agency.dtos.response.CategoryFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ICategoryFullDtoMapper {
    ICategoryFullDtoMapper INSTANCE = Mappers.getMapper( ICategoryFullDtoMapper.class );

    CategoryFullDto toCategoryFullDto(CategoryFullDto categoryFullDto);

    List<CategoryFullDto> toCategoryFullDtoList(List<Category> categories);
}
