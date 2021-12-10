package com.sgu.agency.mappers;

import com.sgu.agency.dal.data.YearRevenueDetail;
import com.sgu.agency.dtos.response.YearRevenueDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IYearRevenueDetailDtoMapper {
    IYearRevenueDetailDtoMapper INSTANCE = Mappers.getMapper( IYearRevenueDetailDtoMapper.class );

    List<YearRevenueDetailDto> toYearRevenueDtoList(List<YearRevenueDetail> yearRevenueDetails);
}
