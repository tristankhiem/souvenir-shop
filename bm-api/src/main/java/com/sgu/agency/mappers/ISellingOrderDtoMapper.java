package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.SellingOrder;
import com.sgu.agency.dtos.response.SellingOrderFullDto;
import com.sgu.agency.dtos.response.SellingOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ISellingOrderDtoMapper {
    ISellingOrderDtoMapper INSTANCE = Mappers.getMapper( ISellingOrderDtoMapper.class );

    SellingOrderDto toSellingOrderDto(SellingOrder employee);
    SellingOrder toSellingOrder(SellingOrderDto categoryDto);

    SellingOrder toSellingOrder(SellingOrderFullDto categoryDto);
    
    List<SellingOrderDto> toSellingOrderDtos(List<SellingOrder> categories);

    SellingOrderFullDto toSellingOrderFullDto(SellingOrder sellingOrder);
}
