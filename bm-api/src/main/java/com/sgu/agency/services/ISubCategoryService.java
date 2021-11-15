package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SubCategoryDto;

import java.util.List;

public interface ISubCategoryService {
    BaseSearchDto<List<SubCategoryDto>> findAll(BaseSearchDto<List<SubCategoryDto>> searchDto);
    List<SubCategoryDto> findAll();
    SubCategoryDto getSubCategoryById(String id);
    SubCategoryDto getByName(String id);
    List<SubCategoryDto> getLikeName(String subCategoryName);
    SubCategoryDto insert(SubCategoryDto employeesDto);
    SubCategoryDto update(SubCategoryDto employeesDto);
    boolean deleteSubCategory(String id);
}
