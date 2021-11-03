package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.RoleDetail;
import com.sgu.agency.dtos.response.RoleDetailDto;
import com.sgu.agency.dtos.response.RoleDetailFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IRoleDetailDtoMapper {
    IRoleDetailDtoMapper INSTANCE = Mappers.getMapper( IRoleDetailDtoMapper.class );

    RoleDetailDto toRoleDetailDto(RoleDetail roleDetail);
    RoleDetailFullDto toRoleDetailFullDto(RoleDetail roleDetail);

    RoleDetail toRoleDetail(RoleDetailDto roleDetailDto);
    RoleDetail toRoleDetailFull(RoleDetailFullDto roleDetailDto);

    List<RoleDetailDto> toRoleDetailListDto(List<RoleDetail> roleDetail);
    List<RoleDetailFullDto> toRoleDetailFullListDto(List<RoleDetail> roleDetail);
}
