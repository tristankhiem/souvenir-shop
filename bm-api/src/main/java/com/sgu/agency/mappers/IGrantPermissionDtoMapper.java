package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.GrantPermission;
import com.sgu.agency.dtos.response.GrantPermissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IGrantPermissionDtoMapper {
    IGrantPermissionDtoMapper INSTANCE = Mappers.getMapper( IGrantPermissionDtoMapper.class );

    GrantPermissionDto toGrantPermissionDto(GrantPermission grantPermission);

    GrantPermission toGrantPermission(GrantPermissionDto grantPermissionDto);

    List<GrantPermissionDto> toGrantPermissionDtoList(List<GrantPermission> grantPermissionList);

    List<GrantPermission> toGrantPermissionList(List<GrantPermissionDto> grantPermissionDtoList);
}
