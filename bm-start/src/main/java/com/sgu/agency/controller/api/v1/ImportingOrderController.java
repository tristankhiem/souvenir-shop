package com.sgu.agency.controller.api.v1;


import com.sgu.agency.biz.services.impl.ImportingOrderServiceImpl;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/importing-order")
public class ImportingOrderController extends BaseController {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ImportingOrderServiceImpl importingOderService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public ImportingOrderController() {

    }
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<ImportingOrderDto>> search) {
        BaseSearchDto<List<ImportingOrderDto>> result = importingOderService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody ImportingOrderFullDto importingOrderFullDto) {
        List<String> errMessages = validateInsert(importingOrderFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        importingOrderFullDto.setEmployee(this.getCurrentEmployee());
        ImportingOrderFullDto importingOrderFull = importingOderService.insert(importingOrderFullDto);


        ResponseEntity<?> res = importingOrderFull != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu phiếu nhập thành công"), HttpStatus.OK.value(), importingOrderFull))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu phiếu nhập"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody ImportingOrderFullDto importingOrderFullDto) {
        List<String> errMessages = validateInsert(importingOrderFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        importingOrderFullDto.setEmployee(this.getCurrentEmployee());
        ImportingOrderFullDto importingOrderFull = importingOderService.update(importingOrderFullDto);


        ResponseEntity<?> res = importingOrderFull != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu phiếu nhập thành công"), HttpStatus.OK.value(), importingOrderFull))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu phiếu nhập"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    private List<String> validateInsert(ImportingOrderFullDto importingOrderFullDto) {
        List<String> result = new ArrayList<>();
//        ImportingOrderFullDto productName = importingOderService.getByName(productDto.getName());
//        if (productName != null) {
//            result.add("Tên sản phẩm đã tồn tại");
//        }

        return result;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errMessages = validateDelete(id);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = importingOderService.delete(id);

        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa phiếu nhập thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa phiếu nhập"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    private List<String> validateDelete(String id) {
        List<String> result = new ArrayList<>();

        if (id.isEmpty()){
            result.add("Quyền không tồn tại");
        }

        return result;
    }
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getImportingOrderFullById(@PathVariable String id) {
        ImportingOrderFullDto importingOrderFullDto = importingOderService.getImportingOrderFullById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), importingOrderFullDto));
    }


}
