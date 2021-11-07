package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.dtos.response.RoleDto;
import com.sgu.agency.dtos.response.RoleFullDto;
import com.sgu.agency.services.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private IRoleService roleService;


    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public RoleController() {
    }

    @PostMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestBody BaseSearchDto<List<RoleDto>> searchDto) {
        BaseSearchDto<List<RoleDto>> search = roleService.findAll(searchDto);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải quyền thành công!"), HttpStatus.OK.value(), search));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeCode(@RequestParam String name) {
        List<RoleDto> roleDtos = roleService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Phiếu xuất"), HttpStatus.OK.value(), roleDtos));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getById(@PathVariable String roleId) {
        RoleFullDto roleFullDto = roleService.getRoleFull(roleId);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải quyền thành công!"), HttpStatus.OK.value(), roleFullDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody RoleFullDto roleFullDto) {
        List<String> errMessages = validateInsert(roleFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        RoleFullDto roles = roleService.insert(roleFullDto);
        ResponseEntity<?> res = roles != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu quyền thành công"), HttpStatus.OK.value(), roles))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu quyền"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody RoleFullDto roleFullDto) {
        List<String> errMessages = validateUpdate(roleFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        RoleFullDto roles = roleService.update(roleFullDto);
        ResponseEntity<?> res = roles != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Cập nhật quyền thành công"), HttpStatus.OK.value(), roles))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi cập nhật quyền"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id) {
        List<String> errMessages = validateDelete(id);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = roleService.delete(id);

        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa quyền thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa quyền"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    private List<String> validateInsert(RoleFullDto roleFullDto) {
        List<String> result = new ArrayList<>();
        RoleFullDto roleName = roleService.getByName(roleFullDto.getName());
        if (roleName != null) {
            result.add("Tên quyền đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdate(RoleFullDto roleFullDto) {
        List<String> result = new ArrayList<>();

        RoleFullDto roleName = roleService.getByName(roleFullDto.getName());
        if (roleName != null && !roleName.getId().equals(roleFullDto.getId())) {
            result.add("Tên quyền đã tồn tại");
        }

        return result;
    }

    private List<String> validateDelete(String id) {
        List<String> result = new ArrayList<>();

        if (id.isEmpty()){
            result.add("Quyền không tồn tại");
        }

        return result;
    }
}

