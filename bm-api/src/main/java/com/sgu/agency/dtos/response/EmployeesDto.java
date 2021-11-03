package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.BlockStatusEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class EmployeesDto {
    private String id;
    private AgencyDto agency;
    @NotEmpty(message = "Tên nhân viên không được trống")
    private String fullName;
    @NotEmpty(message = "Email nhân viên không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotEmpty(message = "Mật khẩu không được trống")
    private String password;
    private BlockStatusEnum blockedStatus;
    private Date birthDate;
}
