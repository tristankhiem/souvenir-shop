package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.PermissionDto;
import com.sgu.agency.dtos.response.RoleDto;

import java.util.List;

public interface IPermissionService {
    List<PermissionDto> findAll();
}
