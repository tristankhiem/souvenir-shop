package com.sgu.agency.controller.api.v1;


import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDetailDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.services.IProductDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-detail")
public class ProductDetailController {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private IProductDetailService productService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public ProductDetailController() {
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<ProductDetailDto> search = productService.findAll();
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công!"), HttpStatus.OK.value(), search));
    }

    @PostMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestBody BaseSearchDto<List<ProductDetailDto>> searchDto) {
        BaseSearchDto<List<ProductDetailDto>> search = productService.findAll(searchDto);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công!"), HttpStatus.OK.value(), search));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeCode(@RequestParam String name) {
        List<ProductDetailDto> productDtos = productService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công"), HttpStatus.OK.value(), productDtos));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable String productId) {
        ProductDetailDto productDto = productService.getById(productId);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công!"), HttpStatus.OK.value(), productDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody ProductDetailDto productDto) {
        List<String> errMessages = validateInsert(productDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        ProductDetailDto product = productService.insert(productDto);
        ResponseEntity<?> res = product != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu sản phẩm thành công"), HttpStatus.OK.value(), product))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu sản phẩm"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody ProductDetailDto productDto) {
        List<String> errMessages = validateUpdate(productDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        ProductDetailDto product = productService.update(productDto);
        ResponseEntity<?> res = product != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Cập nhật sản phẩm thành công"), HttpStatus.OK.value(), product))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi cập nhật sản phẩm"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errMessages = validateDelete(id);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = productService.delete(id);

        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa sản phẩm thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa sản phẩm"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @PostMapping("/upload-image/{productId}")
    public ResponseEntity<?> uploadImage(@ModelAttribute("files") MultipartFile[] files, @PathVariable String productId) throws IOException {
        productService.uploadImageByProductDetailId(files[0], productId);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa sản phẩm thành công"), HttpStatus.OK.value(), null));
    }

    @PostMapping("/delete-image/{productId}")
    public ResponseEntity<?> uploadImage(@PathVariable String productId) throws IOException {
        productService.deleteImageByProductDetailId(productId);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa ảnh thành công"), HttpStatus.OK.value(), null));
    }

    private List<String> validateInsert(ProductDetailDto productDto) {
        List<String> result = new ArrayList<>();
        ProductDetailDto productName = productService.getByName(productDto.getName());
        if (productName != null) {
            result.add("Tên sản phẩm đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdate(ProductDetailDto productDto) {
        List<String> result = new ArrayList<>();

        ProductDetailDto productName = productService.getByName(productDto.getName());
        if (productName != null && !productName.getId().equals(productDto.getId())) {
            result.add("Tên sản phẩm đã tồn tại");
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
