package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.SubCategoryDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.services.ISubCategoryService;
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
@RequestMapping("/api/v1/sub-category")
public class SubCategoryController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ISubCategoryService subCategoryService;


    private static final Logger logger = LoggerFactory.getLogger(SubCategoryController.class);

    public SubCategoryController() {
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<SubCategoryDto>> search) {
        BaseSearchDto<List<SubCategoryDto>> result = subCategoryService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeName(@RequestParam String name) {
        List<SubCategoryDto> subCategoryDtos = subCategoryService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), subCategoryDtos));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        SubCategoryDto subCategorysDto = subCategoryService.getSubCategoryById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), subCategorysDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody SubCategoryDto subCategoryDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(subCategoryDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SubCategoryDto subCategorys = subCategoryService.insert(subCategoryDto);
        ResponseEntity<?> res = subCategorys != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu danh mục con thành công"), HttpStatus.OK.value(), subCategorys))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu danh mục con"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody SubCategoryDto subCategoryDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(subCategoryDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SubCategoryDto subCategorys = subCategoryService.update(subCategoryDto);
        ResponseEntity<?> res = subCategorys != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu danh mục con thành công"), HttpStatus.OK.value(), subCategorys))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu danh mục con"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = subCategoryService.deleteSubCategory(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa danh mục con thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa danh mục con"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }


    private List<String> validateInserting(SubCategoryDto subCategory) {
        List<String> result = new ArrayList<>();
        SubCategoryDto subCategorysEmail = subCategoryService.getByName(subCategory.getName());
        if (subCategorysEmail != null) {
            result.add("Danh mục con đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdating(SubCategoryDto subCategory) {
        List<String> result = new ArrayList<>();
        SubCategoryDto subCategorysEmail = subCategoryService.getByName(subCategory.getName());
        if (subCategorysEmail != null && !Objects.equals(subCategorysEmail.getId(), subCategory.getId())) {
            result.add("Danh mục con đã tồn tại");
        }

        return result;
    }


    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại danh mục con này");
        }

        return msg;
    }

}

