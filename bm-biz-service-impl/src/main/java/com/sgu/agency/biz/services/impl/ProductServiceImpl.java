package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.FileReaderUtil;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.Product;
import com.sgu.agency.dal.entity.ProductDetail;
import com.sgu.agency.dal.repository.IProductDetailRepository;
import com.sgu.agency.dal.repository.IProductRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDetailDto;
import com.sgu.agency.dtos.response.ProductDto;
import com.sgu.agency.dtos.response.ProductFullDto;
import com.sgu.agency.mappers.IProductDetailDtoMapper;
import com.sgu.agency.mappers.IProductDtoMapper;
import com.sgu.agency.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductDetailRepository productDetailRepository;

    @Transactional
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = IProductDtoMapper.INSTANCE.toProductDtoList(products);
        for (ProductDto product : productDtos) {
            if (product.getImageByte() != null) {
                product.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
            }
        }
        return productDtos;
    }

    @Override
    public List<ProductDto> getLikeName(String searchName) {
        List<Product> productDtos = productRepository.getLikeName(searchName);
        return IProductDtoMapper.INSTANCE.toProductDtoList(productDtos);
    }

    @Override
    public List<ProductDto> getListByCategory(String categoryId) {
        List<Product> products = productRepository.getByCategory(categoryId);
        List<ProductDto> productDtos = IProductDtoMapper.INSTANCE.toProductDtoList(products);
        for (ProductDto product : productDtos) {
            if (product.getImageByte() != null) {
                product.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
            }
        }
        return productDtos;
    }

    @Override
    public BaseSearchDto<List<ProductDto>> findAll(BaseSearchDto<List<ProductDto>> searchDto) {
        if (searchDto == null || searchDto.getCurrentPage() == -1 || searchDto.getRecordOfPage() == 0) {
            searchDto.setResult(this.findAll());
            return searchDto;
        }

        Sort sort = null;
        if (searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            sort = searchDto.isSortAsc() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy()) : Sort.by(Sort.Direction.DESC, searchDto.getSortBy());
        }
        PageRequest request = sort == null ? PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage())
                : PageRequest.of(searchDto.getCurrentPage(), searchDto.getRecordOfPage(), sort);

        Page<Product> page = productRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(IProductDtoMapper.INSTANCE.toProductDtoList(page.getContent()));

        for (ProductDto product : searchDto.getResult()) {
            if (product.getImageByte() != null) {
                product.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
            }
        }

        return searchDto;
    }

    @Override
    @Transactional
    public ProductDto insert(ProductDto productDto) {
        try {
            Product product = IProductDtoMapper.INSTANCE.toProduct(productDto);
            product.setId(UUIDHelper.generateType4UUID().toString());
            Product createdProduct = productRepository.save(product);
            ProductDto createdProductDto = IProductDtoMapper.INSTANCE.toProductDto(createdProduct);
            return createdProductDto;
        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public ProductDto getById(String id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = IProductDtoMapper.INSTANCE.toProductDto(product);
        if (product.getImageByte() != null) {
            productDto.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
        }
        return productDto;
    }

    @Override
    public List<ProductDetailDto> getListProductDetailByProductFullId(String id) {
        List<ProductDetail> productDetails = productDetailRepository.getListByProductId(id);
        List<ProductDetailDto> productDetailDtos = IProductDetailDtoMapper.INSTANCE.toProductDetailDtos(productDetails);
        for (ProductDetailDto productDetail : productDetailDtos) {
            if (productDetail.getImageByte() != null) {
                productDetail.setImageByte(FileReaderUtil.decompressBytes(productDetail.getImageByte()));
            }
        }
        return productDetailDtos;
    }
    @Override
    public ProductFullDto getProductFullById(String id)
    {
        Product product = productRepository.findById(id).get();
        ProductFullDto productFullDto = IProductDtoMapper.INSTANCE.toProductFullDto(product);
        if (product.getImageByte() != null) {
            productFullDto.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
        }
        //lay list product detail
        List<ProductDetailDto> productDetailDtos = getListProductDetailByProductFullId(productFullDto.getId());
        productFullDto.setProductDetails(productDetailDtos);
        return productFullDto;
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto productDto) {
        try{
            Product product = IProductDtoMapper.INSTANCE.toProduct(productDto);
            ProductDto oldProduct = getById(product.getId());

            productRepository.save(product);
            return productDto;

        }catch(Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try{
            productRepository.deleteById(id);
            return true;
        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public ProductDto getByName(String name) {
        Product product = productRepository.getByName(name);
        return IProductDtoMapper.INSTANCE.toProductDto(product);
    }

    @Override
    public void uploadImageByProductId(MultipartFile file, String id) throws IOException {
        Product product = productRepository.findById(id).get();
        product.setImageByte(FileReaderUtil.compressBytes(file.getBytes()));
        productRepository.save(product);
    }

    @Override
    public void deleteImageByProductId(String id) {
        Product product = productRepository.findById(id).get();
        product.setImageByte(null);
        product.setImageUrl(null);
        productRepository.save(product);
    }
}
