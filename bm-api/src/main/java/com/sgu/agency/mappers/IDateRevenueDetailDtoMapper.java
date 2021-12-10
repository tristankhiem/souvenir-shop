package com.sgu.agency.mappers;

import com.sgu.agency.dal.data.DateRevenueDetail;
import com.sgu.agency.dtos.response.DateRevenueDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IDateRevenueDetailDtoMapper {
    IDateRevenueDetailDtoMapper INSTANCE = Mappers.getMapper( IDateRevenueDetailDtoMapper.class );

    List<DateRevenueDetailDto> toDateRevenueDtoList(List<DateRevenueDetail> dateRevenueDetails);
}
