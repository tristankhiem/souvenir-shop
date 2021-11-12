package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.SubCategory;
import com.sgu.agency.dal.repository.ISubCategoryRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SubCategoryDto;
import com.sgu.agency.mappers.ISubCategoryDtoMapper;
import com.sgu.agency.services.ISubCategoryService;
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
public class SubCategoryServiceImpl implements ISubCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    @Autowired
    private ISubCategoryRepository subCategoryRepository;

    @Override
    @Transactional
    public List<SubCategoryDto> findAll() {
        List<SubCategory> subCategory = subCategoryRepository.findAll();
        return ISubCategoryDtoMapper.INSTANCE.toSubCategoryDtoList(subCategory);
    }

    @Override
    public List<SubCategoryDto> getLikeName(String subCategoryName) {
        List<SubCategory> subCategory = subCategoryRepository.getLikeName(subCategoryName);
        return ISubCategoryDtoMapper.INSTANCE.toSubCategoryDtoList(subCategory);
    }

    @Override
    @Transactional
    public SubCategoryDto insert(SubCategoryDto subCategoryDto) {
        try {
            SubCategory subCategory = ISubCategoryDtoMapper.INSTANCE.toSubCategory(subCategoryDto);

            subCategory.setId(UUIDHelper.generateType4UUID().toString());
            SubCategory createdSubCategory = subCategoryRepository.save(subCategory);

            subCategoryDto.setId(createdSubCategory.getId());
            return subCategoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public SubCategoryDto update(SubCategoryDto subCategoryDto) {
        try {
            SubCategory old = subCategoryRepository.findById(subCategoryDto.getId()).get();
            SubCategory subCategory = ISubCategoryDtoMapper.INSTANCE.toSubCategory(subCategoryDto);
            subCategoryRepository.save(subCategory);

            return subCategoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteSubCategory(String id) {
        try {
            subCategoryRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public BaseSearchDto<List<SubCategoryDto>> findAll(BaseSearchDto<List<SubCategoryDto>> searchDto) {
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

        Page<SubCategory> page = subCategoryRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ISubCategoryDtoMapper.INSTANCE.toSubCategoryDtoList(page.getContent()));

        return searchDto;
    }

    @Override
    public SubCategoryDto getSubCategoryById(String id) {
        try {
            SubCategory subCategory = subCategoryRepository.findById(id).get();
            SubCategoryDto subCategoryDto = ISubCategoryDtoMapper.INSTANCE.toSubCategoryDto(subCategory);
            return subCategoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public SubCategoryDto getByName(String name) {
        try {
            SubCategory subCategory = subCategoryRepository.getByName(name);
            SubCategoryDto subCategoryDto = ISubCategoryDtoMapper.INSTANCE.toSubCategoryDto(subCategory);
            return subCategoryDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
}
