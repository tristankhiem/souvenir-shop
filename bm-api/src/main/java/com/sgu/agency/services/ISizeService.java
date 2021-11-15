package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SizeDto;

import java.util.List;

public interface ISizeService {
    BaseSearchDto<List<SizeDto>> findAll(BaseSearchDto<List<SizeDto>> searchDto);
    List<SizeDto> findAll();
    SizeDto getSizeById(String id);
    SizeDto getByName(String id);
    List<SizeDto> getLikeName(String sizeName);
    SizeDto insert(SizeDto employeesDto);
    SizeDto update(SizeDto employeesDto);
    boolean deleteSize(String id);
}
