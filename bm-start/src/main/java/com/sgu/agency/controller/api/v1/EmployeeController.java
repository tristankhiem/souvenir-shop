package com.sgu.agency.controller.api.v1;

import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.configuration.security.jwt.UserPrinciple;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.request.LoginDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.EmployeesDto;
import com.sgu.agency.dtos.response.JwtResponseDto;
import com.sgu.agency.dtos.response.ResponseDto;
import com.sgu.agency.dtos.response.security.UserDto;
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

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private IEmployeeService employeeService;


    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController() {
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<EmployeesDto>> search) {
        BaseSearchDto<List<EmployeesDto>> result = employeeService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> search(@PathVariable String id) {
        EmployeesDto employeesDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), employeesDto));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody EmployeesDto employeeDto) {
        // Validation before saving
        List<String> errMessages = validateInserting(employeeDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        EmployeesDto employees = employeeService.insert(employeeDto);
        ResponseEntity<?> res = employees != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu nhân viên thành công"), HttpStatus.OK.value(), employees))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu nhân viên"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody EmployeesDto employeeDto) {
        // Validation before saving
        List<String> errMessages = validateUpdating(employeeDto);
        if (errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        EmployeesDto employees = employeeService.update(employeeDto);
        ResponseEntity<?> res = employees != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu nhân viên thành công"), HttpStatus.OK.value(), employees))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu nhân viên"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errorMessages = validateDelete(id);
        if (errorMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errorMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = employeeService.deleteEmployee(id);
        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa nhân viên thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa nhân viên"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }


    private List<String> validateInserting(EmployeesDto employee) {
        List<String> result = new ArrayList<>();
        employee.setPassword(BCryptHelper.encrypt(employee.getPassword()));
        EmployeesDto employeesEmail = employeeService.getEmployeeByEmailCompany(employee.getEmail());
        if (employeesEmail != null) {
            result.add("Email nhân viên đã tồn tại");
        }

        return result;
    }

    private List<String> validateUpdating(EmployeesDto employee) {
        List<String> result = new ArrayList<>();
        EmployeesDto employeesEmail = employeeService.getEmployeeByEmailCompany(employee.getEmail());
        if (employeesEmail != null && !Objects.equals(employeesEmail.getId(), employee.getId())) {
            result.add("Email nhân viên đã tồn tại");
        }

        return result;
    }


    private List<String> validateDelete(String id) {
        List<String> msg = new ArrayList<>();

        if (id.isEmpty()) {
            msg.add("Không tồn tại nhân viên này");
        }

        return msg;
    }

}

