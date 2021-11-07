package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.RoleDto;
import com.sgu.agency.dtos.response.RoleFullDto;

import java.util.List;

public interface IRoleService {
    List<RoleDto> getLikeName(String name);
    BaseSearchDto<List<RoleDto>> findAll(BaseSearchDto<List<RoleDto>> searchDto);
    RoleFullDto insert(RoleFullDto roleFullDto);
    RoleFullDto getRoleFull(String id);
    RoleFullDto update(RoleFullDto roleFullDto);
    boolean delete(String id);
    RoleFullDto getByName(String name);

}
