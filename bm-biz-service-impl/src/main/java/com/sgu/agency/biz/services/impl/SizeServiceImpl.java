package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.Size;
import com.sgu.agency.dal.repository.ISizeRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SizeDto;
import com.sgu.agency.mappers.ISizeDtoMapper;
import com.sgu.agency.services.ISizeService;
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
public class SizeServiceImpl implements ISizeService {
    private static final Logger logger = LoggerFactory.getLogger(SizeServiceImpl.class);

    @Autowired
    private ISizeRepository sizeRepository;

    @Override
    @Transactional
    public List<SizeDto> findAll() {
        List<Size> size = sizeRepository.findAll();
        return ISizeDtoMapper.INSTANCE.toSizeDtoList(size);
    }

    @Override
    public List<SizeDto> getLikeName(String sizeName) {
        List<Size> size = sizeRepository.getLikeName(sizeName);
        return ISizeDtoMapper.INSTANCE.toSizeDtoList(size);
    }

    @Override
    @Transactional
    public SizeDto insert(SizeDto sizeDto) {
        try {
            Size size = ISizeDtoMapper.INSTANCE.toSize(sizeDto);

            size.setId(UUIDHelper.generateType4UUID().toString());
            Size createdSize = sizeRepository.save(size);

            sizeDto.setId(createdSize.getId());
            return sizeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public SizeDto update(SizeDto sizeDto) {
        try {
            Size old = sizeRepository.findById(sizeDto.getId()).get();
            Size size = ISizeDtoMapper.INSTANCE.toSize(sizeDto);
            sizeRepository.save(size);

            return sizeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteSize(String id) {
        try {
            sizeRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public BaseSearchDto<List<SizeDto>> findAll(BaseSearchDto<List<SizeDto>> searchDto) {
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

        Page<Size> page = sizeRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(ISizeDtoMapper.INSTANCE.toSizeDtoList(page.getContent()));

        return searchDto;
    }

    @Override
    public SizeDto getSizeById(String id) {
        try {
            Size size = sizeRepository.findById(id).get();
            SizeDto sizeDto = ISizeDtoMapper.INSTANCE.toSizeDto(size);
            return sizeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public SizeDto getByName(String name) {
        try {
            Size size = sizeRepository.getByName(name);
            SizeDto sizeDto = ISizeDtoMapper.INSTANCE.toSizeDto(size);
            return sizeDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
}
