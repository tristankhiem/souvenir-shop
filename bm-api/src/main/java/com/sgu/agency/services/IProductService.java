package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    List<ProductDto> getLikeName(String searchName);
    BaseSearchDto<List<ProductDto>> findAll(BaseSearchDto<List<ProductDto>> searchDto);
    ProductDto insert(ProductDto productDto);
    ProductDto getById(String id);
    ProductDto update(ProductDto productDto);
    boolean delete(String id);
    ProductDto getByName(String name);
    void uploadImageByProductId(MultipartFile file, String id);
    String getEncodedBase64ImageByProductId(String id);
}
