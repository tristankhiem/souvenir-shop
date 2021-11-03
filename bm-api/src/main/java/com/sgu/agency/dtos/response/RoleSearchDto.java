package com.sgu.agency.dtos.response;

import com.sgu.agency.dtos.request.BaseSearchDto;
import lombok.Data;

import java.util.List;

@Data
public class RoleSearchDto extends BaseSearchDto<List<RoleDto>> {
    boolean SearchAllAgency;
    String agencyId;
}
