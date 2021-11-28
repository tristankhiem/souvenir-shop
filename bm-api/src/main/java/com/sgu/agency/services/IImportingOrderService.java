package com.sgu.agency.services;

import com.sgu.agency.dal.entity.ImportingOrder;
import com.sgu.agency.dal.entity.ImportingTransaction;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;

import java.util.List;

public interface IImportingOrderService {
    BaseSearchDto<List<ImportingOrderDto>> findAll(BaseSearchDto<List<ImportingOrderDto>> searchDto);
    List<ImportingOrderDto> findAll();
    ImportingOrderFullDto insert(ImportingOrderFullDto importingOrderFullDtoDto);
    boolean delete(String id);
    ImportingOrderFullDto getImportingOrderFullById(String id);
    List<ImportingTransaction> getListImportingTransactionByImportingOrderId (String id);



}
