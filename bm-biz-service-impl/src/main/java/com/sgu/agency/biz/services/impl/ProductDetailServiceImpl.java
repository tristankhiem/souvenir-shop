package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.FileReaderUtil;
import com.sgu.agency.common.utils.UUIDHelper;
import com.sgu.agency.dal.entity.ProductDetail;
import com.sgu.agency.dal.repository.IProductDetailRepository;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.ProductDetailDto;
import com.sgu.agency.mappers.IProductDetailDtoMapper;
import com.sgu.agency.services.IProductDetailService;
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
public class ProductDetailServiceImpl implements IProductDetailService {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    @Autowired
    private IProductDetailRepository productDetailRepository;

    @Transactional
    public List<ProductDetailDto> findAll() {
        List<ProductDetail> products = productDetailRepository.findAll();
        List<ProductDetailDto> productDtos = IProductDetailDtoMapper.INSTANCE.toProductDetailDtos(products);
        for (ProductDetailDto product : productDtos) {
            if (product.getImageByte() != null) {
                product.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
            }
        }
        return productDtos;
    }

    @Override
    public List<ProductDetailDto> getLikeName(String searchName) {
        List<ProductDetail> productDtos = productDetailRepository.getLikeName(searchName);
        return IProductDetailDtoMapper.INSTANCE.toProductDetailDtos(productDtos);
    }

    @Override
    public BaseSearchDto<List<ProductDetailDto>> findAll(BaseSearchDto<List<ProductDetailDto>> searchDto) {
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

        Page<ProductDetail> page = productDetailRepository.findAll(request);
        searchDto.setTotalRecords(page.getTotalElements());
        searchDto.setResult(IProductDetailDtoMapper.INSTANCE.toProductDetailDtos(page.getContent()));

        return searchDto;
    }

    @Override
    @Transactional
    public ProductDetailDto insert(ProductDetailDto productDto) {
        try {
            ProductDetail product = IProductDetailDtoMapper.INSTANCE.toProductDetail(productDto);
            product.setId(UUIDHelper.generateType4UUID().toString());
            ProductDetail createdProductDetail = productDetailRepository.save(product);
            ProductDetailDto createdProductDetailDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(createdProductDetail);
            return createdProductDetailDto;
        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public ProductDetailDto getById(String id) {
        ProductDetail product = productDetailRepository.findById(id).get();
        ProductDetailDto productDto = IProductDetailDtoMapper.INSTANCE.toProductDetailDto(product);
        if (product.getImageByte() != null) {
            productDto.setImageByte(FileReaderUtil.decompressBytes(product.getImageByte()));
        }
        return productDto;
    }

    @Override
    @Transactional
    public ProductDetailDto update(ProductDetailDto productDto) {
        try{
            ProductDetail product = IProductDetailDtoMapper.INSTANCE.toProductDetail(productDto);
            ProductDetailDto oldProductDetail = getById(product.getId());

            productDetailRepository.save(product);
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
            productDetailRepository.deleteById(id);
            return true;
        }catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public ProductDetailDto getByName(String name) {
        ProductDetail product = productDetailRepository.getByName(name);
        return IProductDetailDtoMapper.INSTANCE.toProductDetailDto(product);
    }

    @Override
    public void uploadImageByProductDetailId(MultipartFile file, String id) throws IOException {
        ProductDetail product = productDetailRepository.findById(id).get();
        product.setImageByte(FileReaderUtil.compressBytes(file.getBytes()));
        productDetailRepository.save(product);
    }

    @Override
    public void deleteImageByProductDetailId(String id) {
        ProductDetail product = productDetailRepository.findById(id).get();
        product.setImageByte(null);
        product.setImageUrl(null);
        productDetailRepository.save(product);
    }
}
