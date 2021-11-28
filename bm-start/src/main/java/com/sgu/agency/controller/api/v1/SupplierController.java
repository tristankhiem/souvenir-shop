package com.sgu.agency.controller.api.v1;

import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dal.entity.Supplier;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;
import com.sgu.agency.services.ICustomerService;
import com.sgu.agency.services.ISupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ISupplierService supplierService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public SupplierController() {
    }
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<SupplierDto>> search) {
        BaseSearchDto<List<SupplierDto>> result = supplierService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        SupplierDto supplierDto = supplierService.getSupplierById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), supplierDto));
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody SupplierDto supplierDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(supplierDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SupplierDto suppliers = supplierService.insert(supplierDto);
        ResponseEntity<?> res = suppliers != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu khách hàng thành công"), HttpStatus.OK.value(), suppliers))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu khách hàng"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }
    private List<String> validateInserting(SupplierDto customer) {
        List<String> result = new ArrayList<>();
      //  SupplierDto customerEmail = supplierService.getCustomerByEmailCompany(customer.getEmail());
      //  if (customerEmail != null) {
      //      result.add("Khách hàng đã tồn tại");
     //   }

        return result;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody SupplierDto customerDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(customerDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        SupplierDto customer = supplierService.update(customerDto);
        ResponseEntity<?> res = customer != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu khách hàng thành công"), HttpStatus.OK.value(), customer))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu khách hàng"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    private List<String> validateUpdating(SupplierDto customer) {
        List<String> result = new ArrayList<>();


        return result;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = supplierService.deleteEmployee(id);
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

    @GetMapping("/get-like-name")
    public ResponseEntity<?> getLikeName(@RequestParam String name) {
        List<SupplierDto> supplierDtos = supplierService.getLikeName(name);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), supplierDtos));
    }

}
