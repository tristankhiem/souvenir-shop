package com.sgu.agency.mappers;

import com.sgu.agency.dal.entity.ImportingTransaction;
import com.sgu.agency.dtos.response.ImportingTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IImportingTransactionDtoMapper {
    IImportingTransactionDtoMapper INSTANCE = Mappers.getMapper( IImportingTransactionDtoMapper.class );

    ImportingTransactionDto toImportingTransactionDto(ImportingTransaction employee);
    ImportingTransaction toImportingTransaction(ImportingTransactionDto categoryDto);

    List<ImportingTransactionDto> toImportingTransactionDtos(List<ImportingTransaction> categories);

}
