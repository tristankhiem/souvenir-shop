package com.sgu.agency.services;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    String getEncodedBase64ImageByProductId(String id);

    void uploadImageByProductId(MultipartFile file, String id);
}
