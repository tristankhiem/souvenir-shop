package com.sgu.agency.services;

import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDetailDto;
import com.sgu.agency.dtos.response.ProductDto;
import com.sgu.agency.dtos.response.ProductFullDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    List<ProductDto> findAll();
    List<ProductDto> getLikeName(String searchName);
    List<ProductDto> getListByCategory(String categoryId);
    BaseSearchDto<List<ProductDto>> findAll(BaseSearchDto<List<ProductDto>> searchDto);
    ProductDto insert(ProductDto productDto);
    ProductDto getById(String id);
    ProductDto update(ProductDto productDto);
    boolean delete(String id);
    ProductDto getByName(String name);
    List<ProductDetailDto> getListProductDetailByProductFullId (String id);
    ProductFullDto getProductFullById (String id);
    void uploadImageByProductId(MultipartFile file, String id) throws IOException;
    void deleteImageByProductId(String id) throws IOException;
}
