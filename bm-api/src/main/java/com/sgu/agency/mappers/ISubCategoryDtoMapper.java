package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.SubCategory;
import com.sgu.agency.dtos.response.SubCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ISubCategoryDtoMapper {
    ISubCategoryDtoMapper INSTANCE = Mappers.getMapper( ISubCategoryDtoMapper.class );

    SubCategoryDto toSubCategoryDto(SubCategory employee);
    SubCategory toSubCategory(SubCategoryDto categoryDto);

    List<SubCategoryDto> toSubCategoryDtoList(List<SubCategory> categories);

}
