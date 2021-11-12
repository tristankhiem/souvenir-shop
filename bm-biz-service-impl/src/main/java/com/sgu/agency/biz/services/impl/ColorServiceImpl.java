package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.common.utils.RandomTextHelper;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.Color;
import com.sgu.agency.dal.repository.IColorRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ColorDto;
import com.sgu.agency.mappers.IColorDtoMapper;
import com.sgu.agency.services.IColorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ColorServiceImpl implements IColorService {
    private static final Logger logger = LoggerFactory.getLogger(ColorServiceImpl.class);

    @Autowired
    private IColorRepository colorRepository;

    @Override
    @Transactional
    public List<ColorDto> findAll() {
        List<Color> color = colorRepository.findAll();
        return IColorDtoMapper.INSTANCE.toColorDtoList(color);
    }

    @Override
    public List<ColorDto> getLikeName(String colorName) {
        List<Color> color = colorRepository.getLikeName(colorName);
        return IColorDtoMapper.INSTANCE.toColorDtoList(color);
    }

    @Override
    @Transactional
    public ColorDto insert(ColorDto colorDto) {
        try {
            Color color = IColorDtoMapper.INSTANCE.toColor(colorDto);

            color.setId(UUIDHelper.generateType4UUID().toString());
            Color createdColor = colorRepository.save(color);

            colorDto.setId(createdColor.getId());
            return colorDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public ColorDto update(ColorDto colorDto) {
        try {
            Color old = colorRepository.findById(colorDto.getId()).get();
            Color color = IColorDtoMapper.INSTANCE.toColor(colorDto);
            colorRepository.save(color);

            return colorDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteColor(String id) {
        try {
            colorRepository.deleteById(id);
            return true;
        }  catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public BaseSearchDto<List<ColorDto>> findAll(BaseSearchDto<List<ColorDto>> searchDto) {
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

        Page<Color> page = colorRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(IColorDtoMapper.INSTANCE.toColorDtoList(page.getContent()));

        return searchDto;
    }

    @Override
    public ColorDto getColorById(String id) {
        try {
            Color color = colorRepository.findById(id).get();
            ColorDto colorDto = IColorDtoMapper.INSTANCE.toColorDto(color);
            return colorDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public ColorDto getByName(String name) {
        try {
            Color color = colorRepository.getByName(name);
            ColorDto colorDto = IColorDtoMapper.INSTANCE.toColorDto(color);
            return colorDto;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }
}
