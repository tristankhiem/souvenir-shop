package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Role;
import com.sgu.agency.dtos.response.RoleDto;
import com.sgu.agency.dtos.response.RoleFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IRoleDtoMapper {
    IRoleDtoMapper INSTANCE = Mappers.getMapper( IRoleDtoMapper.class );

    RoleDto toRoleDto(Role role);
    RoleFullDto toRoleFullDto(Role role);
    Role toRole(RoleFullDto roleDto);

    List<RoleDto> toRoleDtoList(List<Role> roleList);
    List<RoleFullDto> toRoleFullDtoList(List<Role> roles);
}
