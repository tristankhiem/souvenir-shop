package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.ImportingOrder;
import com.sgu.agency.dtos.response.ImportingOrderDto;
import com.sgu.agency.dtos.response.ImportingOrderFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IImportingOrderDtoMapper {
    IImportingOrderDtoMapper INSTANCE = Mappers.getMapper( IImportingOrderDtoMapper.class );

    ImportingOrderDto toImportingOrderDto(ImportingOrder importingOrder);
    ImportingOrder toImportingOrder(ImportingOrderDto categoryDto);

    ImportingOrder toImportingOrder(ImportingOrderFullDto categoryDto);

    List<ImportingOrderDto> toImportingOrderDtos(List<ImportingOrder> importingOrders);

    ImportingOrderFullDto toImportingOrderFullDto(ImportingOrder importingOrder);

}
