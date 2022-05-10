package com.sgu.agency.controller.api.v1;

import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.configuration.security.jwt.UserPrinciple;
import com.sgu.agency.dtos.request.LoginDto;
import com.sgu.agency.dtos.response.CustomerDto;
import com.sgu.agency.dtos.response.EmployeeFullDto;
import com.sgu.agency.dtos.response.JwtResponseDto;
import com.sgu.agency.dtos.response.ResponseDto;
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
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private ICustomerService customerService;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @Valid @RequestBody LoginDto user) throws IOException, JAXBException {
        logger.info("login api");

        // Validate employee account
        EmployeeFullDto employeesDto = employeeService.getEmployeeFull(user.getUsername());
        if(employeesDto == null || !BCryptHelper.checkData(user.getPassword(), employeesDto.getPassword())) {
            return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tên đăng nhập hoặc password không đúng"), HttpStatus.BAD_GATEWAY.value(), ""));
        }

        UserDto userDto = employeesDto.toUserDto();

        userDto.setUserModel(UserModelEnum.EMPLOYEE);

        UserPrinciple userDetail = UserPrinciple.build(userDto);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetail, null, userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());

        // Reset password
        userDto.setPassword("");

        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Đăng nhập thành công"), HttpStatus.OK.value(), new JwtResponseDto(jwt, userDto)));
    }

    @PostMapping("/login-customer")
    public ResponseEntity<?> loginCustomer(HttpServletRequest request, @Valid @RequestBody LoginDto user) throws IOException, JAXBException {
        logger.info("login api");

        // Validate employee account
        CustomerDto customerDto = customerService.getCustomerByEmail(user.getUsername());
        if(customerDto == null || !BCryptHelper.checkData(user.getPassword(), customerDto.getPassword()) || customerDto.getIsValid()==false) {
            return ResponseEntity.ok(new ResponseDto(Arrays.asList("Tên đăng nhập hoặc password không đúng"), HttpStatus.BAD_GATEWAY.value(), ""));
        }

        UserDto userDto = customerDto.toUserDto();

        userDto.setUserModel(UserModelEnum.CUSTOMER);

        UserPrinciple userDetail = UserPrinciple.build(userDto);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetail, null, userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());

        // Reset password
        userDto.setPassword("");

        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Đăng nhập thành công"), HttpStatus.OK.value(), new JwtResponseDto(jwt, userDto)));
    }

}

