package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Color;
import com.sgu.agency.dtos.response.ColorDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IColorDtoMapper {
    IColorDtoMapper INSTANCE = Mappers.getMapper( IColorDtoMapper.class );

    ColorDto toColorDto(Color employee);
    Color toColor(ColorDto categoryDto);
    
    List<ColorDto> toColorDtoList(List<Color> categories);

}
