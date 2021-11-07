package com.sgu.agency.biz.services.impl;

import com.sgu.agency.dal.entity.Permission;
import com.sgu.agency.dal.repository.IPermissionRepository;
import com.sgu.agency.dtos.response.PermissionDto;
import com.sgu.agency.mappers.IPermissionDtoMapper;
import com.sgu.agency.services.IPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private IPermissionRepository permissionRepository;

    @Transactional
    public List<PermissionDto> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return IPermissionDtoMapper.INSTANCE.toPermissionDtoList(permissions);
    }
}
