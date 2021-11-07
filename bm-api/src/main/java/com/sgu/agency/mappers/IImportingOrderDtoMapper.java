package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.ImportingOrder;
import com.sgu.agency.dtos.response.ImportingOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IImportingOrderDtoMapper {
    IImportingOrderDtoMapper INSTANCE = Mappers.getMapper( IImportingOrderDtoMapper.class );

    ImportingOrderDto toImportingOrderDto(ImportingOrder employee);
    ImportingOrder toImportingOrder(ImportingOrderDto categoryDto);

    List<ImportingOrderDto> toImportingOrderDtos(List<ImportingOrder> categories);

}
