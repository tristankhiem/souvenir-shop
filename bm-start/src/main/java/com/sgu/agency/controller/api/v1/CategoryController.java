package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.CategoryFullDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.dtos.response.CategoryDto;
import com.sgu.agency.dtos.response.SubCategoryDto;
import com.sgu.agency.services.ICategoryService;
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
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ICategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    public CategoryController() {
    }
    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<CategoryFullDto> result =  categoryService.findAllCategoryFull();
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<CategoryDto>> search) {
        BaseSearchDto<List<CategoryDto>> result = categoryService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeName(@RequestParam String name) {
        List<CategoryDto> categoryDtos = categoryService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), categoryDtos));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        CategoryDto categorysDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), categorysDto));
    }

//    @GetMapping("/get-subcategories/{id}")
//    public ResponseEntity<?> getSubcategories(@PathVariable String id) {
//       SubCategoryDto subcategorysDto = categoryService.getSubcategoryById(id);
//       System.out.println("/get-subcategories/{id} "+subcategorysDto.getId());
//       return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), subcategorysDto));
//    }
//
    @GetMapping("/get-subcategories-by-category/{id}")
    public ResponseEntity<?> getSubcategoriesByCategory(@PathVariable String id) {
        List<SubCategoryDto> subcategorysDto = categoryService.getSubcategoriesByCategory(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), subcategorysDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody CategoryDto categoryDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(categoryDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        CategoryDto categorys = categoryService.insert(categoryDto);
        ResponseEntity<?> res = categorys != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu danh mục hàng hóa thành công"), HttpStatus.OK.value(), categorys))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu danh mục hàng hóa"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CategoryDto categoryDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(categoryDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        CategoryDto categorys = categoryService.update(categoryDto);
        ResponseEntity<?> res = categorys != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu danh mục hàng hóa thành công"), HttpStatus.OK.value(), categorys))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu danh mục hàng hóa"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = categoryService.deleteCategory(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa danh mục hàng hóa thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa danh mục hàng hóa"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }


    private List<String> validateInserting(CategoryDto category) {
        List<String> result = new ArrayList<>();
        CategoryDto categorysEmail = categoryService.getByName(category.getName());
        if (categorysEmail != null) {
            result.add("Danh mục hàng hóa đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdating(CategoryDto category) {
        List<String> result = new ArrayList<>();
        CategoryDto categorysEmail = categoryService.getByName(category.getName());
        if (categorysEmail != null && !Objects.equals(categorysEmail.getId(), category.getId())) {
            result.add("Danh mục hàng hóa đã tồn tại");
        }

        return result;
    }


    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại danh mục hàng hóa này");
        }

        return msg;
    }

    @GetMapping("/get-subcategories/{id}")
    public ResponseEntity<?> getSubcategories(@PathVariable String id) {
        List<SubCategoryDto> subcategorysDto = categoryService.getSubcategoriesByCategory(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), subcategorysDto));
    }
}

