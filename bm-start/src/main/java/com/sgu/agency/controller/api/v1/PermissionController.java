package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.PermissionDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.dtos.response.RoleDto;
import com.sgu.agency.services.IPermissionService;
import com.sgu.agency.services.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private IPermissionService permissionService;


    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    public PermissionController() {
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        List<PermissionDto> search = permissionService.findAll();
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải quyền thành công!"), HttpStatus.OK.value(), search));
    }
}

