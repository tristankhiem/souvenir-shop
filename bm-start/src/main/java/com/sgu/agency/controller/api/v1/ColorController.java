package com.sgu.agency.controller.api.v1;

import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ColorDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.dtos.response.RoleDto;
import com.sgu.agency.services.IColorService;
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
@RequestMapping("/api/v1/color")
public class ColorController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private IColorService colorService;


    private static final Logger logger = LoggerFactory.getLogger(ColorController.class);

    public ColorController() {
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<ColorDto>> search) {
        BaseSearchDto<List<ColorDto>> result = colorService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeName(@RequestParam String name) {
        List<ColorDto> colorDtos = colorService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), colorDtos));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        ColorDto colorsDto = colorService.getColorById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), colorsDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody ColorDto colorDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(colorDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        ColorDto colors = colorService.insert(colorDto);
        ResponseEntity<?> res = colors != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu màu sắc thành công"), HttpStatus.OK.value(), colors))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu màu sắc"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody ColorDto colorDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(colorDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        ColorDto colors = colorService.update(colorDto);
        ResponseEntity<?> res = colors != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu màu sắc thành công"), HttpStatus.OK.value(), colors))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu màu sắc"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = colorService.deleteColor(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa màu sắc thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa màu sắc"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }


    private List<String> validateInserting(ColorDto color) {
        List<String> result = new ArrayList<>();
        ColorDto colorsEmail = colorService.getByName(color.getName());
        if (colorsEmail != null) {
            result.add("Màu sắc đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdating(ColorDto color) {
        List<String> result = new ArrayList<>();
        ColorDto colorsEmail = colorService.getByName(color.getName());
        if (colorsEmail != null && !Objects.equals(colorsEmail.getId(), color.getId())) {
            result.add("Màu sắc đã tồn tại");
        }

        return result;
    }


    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại màu sắc này");
        }

        return msg;
    }

}

