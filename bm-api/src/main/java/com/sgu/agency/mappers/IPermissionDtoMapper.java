package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Permission;
import com.sgu.agency.dtos.response.PermissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IPermissionDtoMapper {
    IPermissionDtoMapper INSTANCE = Mappers.getMapper( IPermissionDtoMapper.class );

    PermissionDto toPermissionDto(Permission permission);

    Permission toPermission(PermissionDto permissionDto);

    List<PermissionDto> toPermissionDtoList(List<Permission> permissionList);
}
