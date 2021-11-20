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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/importing-order")
public class ImportingOrderController extends BaseController {
    @Autowired
    JwtProvider jwtProvider;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public ImportingOrderController() {

    }
//    Lưu ý: Khi tạo phiếu nhập hàng thì gán Employee bằng hàm this.getCurrentEmployee() để lấy thông tin nhân viên đang đăng nhập gán vào
//        nội dung phiếu.

}
