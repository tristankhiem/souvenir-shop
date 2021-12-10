package com.sgu.agency.services;

import com.sgu.agency.dal.entity.SellingTransaction;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;

import java.util.List;

public interface ISellingOrderService {
    BaseSearchDto<List<SellingOrderDto>> findAll(BaseSearchDto<List<SellingOrderDto>> searchDto);
    List<SellingOrderDto> findAll();
    SellingOrderFullDto insert(SellingOrderFullDto sellingOrderFullDtoDto);
    SellingOrderFullDto update(SellingOrderFullDto sellingOrderFullDto);
    boolean delete(String id);
    SellingOrderFullDto getSellingOrderFullById(String id);
    List<SellingTransaction> getListSellingTransactionBySellingOrderId (String id);
    List<SellingOrderFullDto> getByCustomerId(String id);
    List<MonthRevenueDetailDto> getMonthRevenue(RangeDateDto rangeDateDto);
    List<DateRevenueDetailDto> getDateRevenue(RangeDateDto rangeDateDto);
    List<YearRevenueDetailDto> getYearRevenue(RangeDateDto rangeDateDto);

}
