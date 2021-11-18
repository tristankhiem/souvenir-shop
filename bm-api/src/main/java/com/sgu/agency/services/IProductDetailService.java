package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDetailDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductDetailService {
    List<ProductDetailDto> findAll();
    List<ProductDetailDto> getLikeName(String searchName);
    BaseSearchDto<List<ProductDetailDto>> findAll(BaseSearchDto<List<ProductDetailDto>> searchDto);
    ProductDetailDto insert(ProductDetailDto productDto);
    ProductDetailDto getById(String id);
    ProductDetailDto update(ProductDetailDto productDto);
    boolean delete(String id);
    ProductDetailDto getByName(String name);
    void uploadImageByProductDetailId(MultipartFile file, String id) throws IOException;
    void deleteImageByProductDetailId(String id) throws IOException;
}
