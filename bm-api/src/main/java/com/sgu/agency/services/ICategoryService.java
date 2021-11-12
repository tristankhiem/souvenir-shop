package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.CategoryDto;

import java.util.List;

public interface ICategoryService {
    BaseSearchDto<List<CategoryDto>> findAll(BaseSearchDto<List<CategoryDto>> searchDto);
    List<CategoryDto> findAll();
    CategoryDto getCategoryById(String id);
    CategoryDto getByName(String id);
    List<CategoryDto> getLikeName(String categoryName);
    CategoryDto insert(CategoryDto employeesDto);
    CategoryDto update(CategoryDto employeesDto);
    boolean deleteCategory(String id);
}
