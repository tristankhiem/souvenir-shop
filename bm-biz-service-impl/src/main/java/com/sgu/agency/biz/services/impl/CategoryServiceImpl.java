package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.Category;
import com.sgu.agency.dal.entity.SubCategory;
import com.sgu.agency.dal.repository.ICategoryRepository;
import com.sgu.agency.dal.repository.ISubCategoryRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.CategoryDto;
import com.sgu.agency.dtos.response.CategoryFullDto;
import com.sgu.agency.dtos.response.SubCategoryDto;
import com.sgu.agency.mappers.ICategoryDtoMapper;
import com.sgu.agency.mappers.ICategoryFullDtoMapper;
import com.sgu.agency.mappers.ISubCategoryDtoMapper;
import com.sgu.agency.services.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private ISubCategoryRepository subCategoryRepository;

    @Override
    @Transactional
    public List<CategoryDto> findAll() {
        List<Category> category = categoryRepository.findAll();
        return ICategoryDtoMapper.INSTANCE.toCategoryDtoList(category);
    }

    @Override
    public List<CategoryFullDto> findAllCategoryFull() {
        List<Category> categories= categoryRepository.findAll();
        List<CategoryFullDto> categoryFullDtoList=ICategoryFullDtoMapper.INSTANCE.toCategoryFullDtoList(categories);
        for (CategoryFullDto s: categoryFullDtoList)
        {
            List<SubCategory> subCategory = subCategoryRepository.findByIdCategory(s.getId());
            List<SubCategoryDto> subCategoryDtos = ISubCategoryDtoMapper.INSTANCE.toSubCategoryDtoList(subCategory);

            s.setSubCategories(subCategoryDtos);
        }

        return categoryFullDtoList;

    }

    @Override
    public List<CategoryDto> getLikeName(String categoryName) {
        List<Category> category = categoryRepository.getLikeName(categoryName);
        return ICategoryDtoMapper.INSTANCE.toCategoryDtoList(category);
    }

    @Override
    @Transactional
    public CategoryDto insert(CategoryDto categoryDto) {
        try {
            Category category = ICategoryDtoMapper.INSTANCE.toCategory(categoryDto);

            category.setId(UUIDHelper.generateType4UUID().toString());
            Category createdCategory = categoryRepository.save(category);

            categoryDto.setId(createdCategory.getId());
            return categoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        try {
            Category old = categoryRepository.findById(categoryDto.getId()).get();
            Category category = ICategoryDtoMapper.INSTANCE.toCategory(categoryDto);
            categoryRepository.save(category);

            return categoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteCategory(String id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public BaseSearchDto<List<CategoryDto>> findAll(BaseSearchDto<List<CategoryDto>> searchDto) {
        if(searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll());
            return searchDto;
        }

        Sort sort = null;
        if(searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<Category> page = categoryRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ICategoryDtoMapper.INSTANCE.toCategoryDtoList(page.getContent()));

        return searchDto;
    }

    @Override
    public CategoryDto getCategoryById(String id) {
        try {
            Category category = categoryRepository.findById(id).get();
            CategoryDto categoryDto = ICategoryDtoMapper.INSTANCE.toCategoryDto(category);
            return categoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public CategoryDto getByName(String name) {
        try {
            Category category = categoryRepository.getByName(name);
            CategoryDto categoryDto = ICategoryDtoMapper.INSTANCE.toCategoryDto(category);
            return categoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
}
