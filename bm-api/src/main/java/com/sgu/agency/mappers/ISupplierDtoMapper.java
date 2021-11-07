package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Supplier;
import com.sgu.agency.dtos.response.SupplierDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ISupplierDtoMapper {
    ISupplierDtoMapper INSTANCE = Mappers.getMapper( ISupplierDtoMapper.class );

    SupplierDto toSupplierDto(Supplier employee);
    Supplier toSupplier(SupplierDto categoryDto);

    List<SupplierDto> toSupplierDtos(List<Supplier> categories);

}
