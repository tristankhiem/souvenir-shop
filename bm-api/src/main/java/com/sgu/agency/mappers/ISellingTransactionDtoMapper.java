package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.SellingTransaction;
import com.sgu.agency.dtos.response.SellingTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ISellingTransactionDtoMapper {
    ISellingTransactionDtoMapper INSTANCE = Mappers.getMapper( ISellingTransactionDtoMapper.class );

    SellingTransactionDto toSellingTransactionDto(SellingTransaction employee);
    SellingTransaction toSellingTransaction(SellingTransactionDto categoryDto);

    List<SellingTransactionDto> toSellingTransactionDtos(List<SellingTransaction> categories);

}
