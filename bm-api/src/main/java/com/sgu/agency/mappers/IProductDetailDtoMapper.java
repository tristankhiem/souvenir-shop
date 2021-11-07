package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.ProductDetail;
import com.sgu.agency.dtos.response.ProductDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IProductDetailDtoMapper {
    IProductDetailDtoMapper INSTANCE = Mappers.getMapper( IProductDetailDtoMapper.class );

    ProductDetailDto toProductDetailDto(ProductDetail employee);
    ProductDetail toProduct(ProductDetailDto categoryDto);

    List<ProductDetailDto> toProductDetailDtos(List<ProductDetail> categories);

}
