package com.sgu.agency.controller.api.v1;

import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.configuration.security.jwt.UserPrinciple;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.LoginDto;
import com.sgu.agency.dtos.response.*;
import com.sgu.agency.dtos.response.security.UserDto;
import com.sgu.agency.services.ICustomerService;
import com.sgu.agency.services.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ICustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public CustomerController() {
    }
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<CustomerDto>> search) {
        BaseSearchDto<List<CustomerDto>> result = customerService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), customerDto));
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody CustomerDto customerDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(customerDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        CustomerDto customers = customerService.insert(customerDto);
        ResponseEntity<?> res = customers != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu khách hàng thành công"), HttpStatus.OK.value(), customers))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu khách hàng"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }
    private List<String> validateInserting(CustomerDto customer) {
        List<String> result = new ArrayList<>();
        customer.setPassword(BCryptHelper.encode(customer.getPassword()));
        CustomerDto customerEmail = customerService.getCustomerByEmailCompany(customer.getEmail());
        if (customerEmail != null) {
            result.add("Khách hàng đã tồn tại");
        }

        return result;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CustomerDto customerDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(customerDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        CustomerDto customer = customerService.update(customerDto);
        ResponseEntity<?> res = customer != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu khách hàng thành công"), HttpStatus.OK.value(), customer))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu khách hàng"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    private List<String> validateUpdating(CustomerDto customer) {
        List<String> result = new ArrayList<>();
        CustomerDto customerEmail = customerService.getCustomerByEmailCompany(customer.getEmail());
        if (customerEmail != null && !Objects.equals(customerEmail.getId(), customerEmail.getId())) {
            result.add("Email khách hàng đã tồn tại");
        }

        return result;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = customerService.deleteEmployee(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa nhân viên thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa nhân viên"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại nhân viên này");
        }

        return msg;
    }
    @PutMapping("/change-account-state")
    public ResponseEntity<?> changeAccountState(@Valid @RequestBody CustomerDto customerDto) {
        // Validation before saving

        CustomerDto customer = customerService.changeAccountState(customerDto);
        ResponseEntity<?> res = customer != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu khách hàng thành công"), HttpStatus.OK.value(), customer))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu khách hàng"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }




}
