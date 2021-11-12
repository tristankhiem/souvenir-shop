package com.sgu.agency.biz.services.impl;

import com.sgu.agency.common.utils.FileReaderUtil;
import com.sgu.agency.common.utils.FileUploadUtil;
import com.sgu.agency.common.utils.TemporaryLocalStorage;
import com.sgu.agency.dal.repository.IProductRepository;
import com.sgu.agency.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Service
public class ImageServiceImpl implements IImageService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private FileReaderUtil fileReaderUtil;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Override
    public String getEncodedBase64ImageByProductId(String id) {
        return productRepository.findById(id).map((product) -> {
            //Get image name absolute path from Employee instance
            String imageName = product.getImageUrl()    ;

            //Check if TEMP_IMAGES_STORAGE contains the temp image name, if it exists the immediately return
            // the image encoded in base64.
            if (TemporaryLocalStorage.TEMP_IMAGES_STORAGE.containsKey(imageName)) {
                return TemporaryLocalStorage.TEMP_IMAGES_STORAGE.get(imageName);
            }

            //Read file content from disk with asssociated image file name
            byte[] fileContent = fileReaderUtil.readFileFromDisk(imageName);

            //Convert image file content to base64 type.
            String encodedString = fileReaderUtil.getConvertedBase64ImageContentFromImageByteContent(fileContent);

            //Map the encoding to the associated image name.
            return TemporaryLocalStorage.mapAndGetEncodedStringToAssociatedImageName(imageName, encodedString);

        }) //If employee is not found based on the given employee id then throw the exception.
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void uploadImageByProductId(MultipartFile file, String id) {
        productRepository.findById(id).map((product) -> {
            String imageAbsoluteName = fileUploadUtil
                    .getPreparedImageFileNameWithAssociatedEmployeeId(String.valueOf(id));
            product.setImageUrl(imageAbsoluteName);
            fileUploadUtil.writePNGImageFileToDisk(imageAbsoluteName, file);
            return productRepository.save(product);
        }).orElseThrow(NoSuchElementException::new);
    }
}
