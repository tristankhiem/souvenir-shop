package com.sgu.agency.controller.api.v1;

import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.services.IImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private IImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public ImageController() {
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getEncodedBase64ImageByProductId(id));
    }

    @PostMapping()
    public ResponseEntity<?> uploadImage(@RequestPart("imageFile") MultipartFile file,
                                         @RequestParam("id") String id) {
        imageService.uploadImageByProductId(file, id);
        return ResponseEntity.ok(imageService.getEncodedBase64ImageByProductId(id));
    }

//    @GetMapping()
//    public ResponseEntity<?> getAllEmployeesProfileImage() {
//        return ResponseEntity.ok(employeeService.getAllEmployeesProfileImage(EmployeeDetailsDto.class));
//    }
}
