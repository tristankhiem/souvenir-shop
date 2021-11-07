package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.BlockStatusEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class EmployeesDto {
    private String id;
    @NotEmpty(message = "Tên nhân viên không được trống")
    private String name;
    @NotEmpty(message = "Số điện thoại không được trống")
    private String phone;
    @NotEmpty(message = "Email nhân viên không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotEmpty(message = "Mật khẩu không được trống")
    private String password;
    private Date birthDate;
    private RoleDto role;
    private Date createdDate;
    private Date updatedDate;
}
