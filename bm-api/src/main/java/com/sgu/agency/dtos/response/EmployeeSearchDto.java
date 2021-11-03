package com.sgu.agency.dtos.response;

import com.sgu.agency.dtos.request.BaseSearchDto;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeSearchDto extends BaseSearchDto<List<EmployeeDetailDto>> {
    String fullName;
    String email;
    String agencyId;
    boolean searchAllAgency;
}
