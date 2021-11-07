package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Size;
import com.sgu.agency.dtos.response.SizeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ISizeDtoMapper {
    ISizeDtoMapper INSTANCE = Mappers.getMapper( ISizeDtoMapper.class );

    SizeDto toSizeDto(Size employee);
    Size toSize(SizeDto categoryDto);

    List<SizeDto> toSizeDtos(List<Size> categories);

}
