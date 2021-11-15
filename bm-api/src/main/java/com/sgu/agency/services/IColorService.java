package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ColorDto;
import com.sgu.agency.dtos.response.RangeDateDto;

import java.util.List;

public interface IColorService {
    BaseSearchDto<List<ColorDto>> findAll(BaseSearchDto<List<ColorDto>> searchDto);
    List<ColorDto> findAll();
    ColorDto getColorById(String id);
    ColorDto getByName(String id);
    List<ColorDto> getLikeName(String colorName);
    ColorDto insert(ColorDto employeesDto);
    ColorDto update(ColorDto employeesDto);
    boolean deleteColor(String id);
}
