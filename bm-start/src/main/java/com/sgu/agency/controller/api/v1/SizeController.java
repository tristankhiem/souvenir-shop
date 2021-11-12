package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SizeDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.services.ISizeService;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/size")
public class SizeController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ISizeService sizeService;


    private static final Logger logger = LoggerFactory.getLogger(SizeController.class);

    public SizeController() {
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<SizeDto>> search) {
        BaseSearchDto<List<SizeDto>> result = sizeService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeName(@RequestParam String name) {
        List<SizeDto> sizeDtos = sizeService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), sizeDtos));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        SizeDto sizesDto = sizeService.getSizeById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), sizesDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody SizeDto sizeDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(sizeDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SizeDto sizes = sizeService.insert(sizeDto);
        ResponseEntity<?> res = sizes != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu kích thước thành công"), HttpStatus.OK.value(), sizes))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu kích thước"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody SizeDto sizeDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(sizeDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SizeDto sizes = sizeService.update(sizeDto);
        ResponseEntity<?> res = sizes != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu kích thước thành công"), HttpStatus.OK.value(), sizes))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu kích thước"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = sizeService.deleteSize(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa kích thước thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa kích thước"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }


    private List<String> validateInserting(SizeDto size) {
        List<String> result = new ArrayList<>();
        SizeDto sizesEmail = sizeService.getByName(size.getName());
        if (sizesEmail != null) {
            result.add("Kích thước đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdating(SizeDto size) {
        List<String> result = new ArrayList<>();
        SizeDto sizesEmail = sizeService.getByName(size.getName());
        if (sizesEmail != null && !Objects.equals(sizesEmail.getId(), size.getId())) {
            result.add("Kích thước đã tồn tại");
        }

        return result;
    }


    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại kích thước này");
        }

        return msg;
    }

}

