package com.sgu.agency.controller.api.v1;


import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private IProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public ProductController() {
    }

    @PostMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestBody BaseSearchDto<List<ProductDto>> searchDto) {
        BaseSearchDto<List<ProductDto>> search = productService.findAll(searchDto);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công!"), HttpStatus.OK.value(), search));
    }

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeCode(@RequestParam String name) {
        List<ProductDto> productDtos = productService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Phiếu xuất"), HttpStatus.OK.value(), productDtos));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable String productId) {
        ProductDto productDto = productService.getById(productId);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tải sản phẩm thành công!"), HttpStatus.OK.value(), productDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody ProductDto productDto) {
        List<String> errMessages = validateInsert(productDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        ProductDto product = productService.insert(productDto);
        ResponseEntity<?> res = product != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu sản phẩm thành công"), HttpStatus.OK.value(), product))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu sản phẩm"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody ProductDto productDto) {
        List<String> errMessages = validateUpdate(productDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        ProductDto product = productService.update(productDto);
        ResponseEntity<?> res = product != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Cập nhật sản phẩm thành công"), HttpStatus.OK.value(), product))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi cập nhật sản phẩm"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id) {
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
    public ResponseEntity<?> uploadImage(@ModelAttribute("files") MultipartFile[] files, @PathVariable String productId) {
        productService.uploadImageByProductId(files[0], productId);
        return ResponseEntity.ok(productService.getEncodedBase64ImageByProductId(productId));
    }

    private List<String> validateInsert(ProductDto productDto) {
        List<String> result = new ArrayList<>();
        ProductDto productName = productService.getByName(productDto.getName());
        if (productName != null) {
            result.add("Tên sản phẩm đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdate(ProductDto productDto) {
        List<String> result = new ArrayList<>();

        ProductDto productName = productService.getByName(productDto.getName());
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
