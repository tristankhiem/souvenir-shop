package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.Product;
import com.sgu.agency.dal.entity.Role;
import com.sgu.agency.dtos.response.ProductDto;
import com.sgu.agency.dtos.response.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IProductDtoMapper {
    IProductDtoMapper INSTANCE = Mappers.getMapper( IProductDtoMapper.class );

    ProductDto toProductDto(Product employee);
    Product toProduct(ProductDto categoryDto);

    List<ProductDto> toProductDtoList(List<Product> productList);

}
