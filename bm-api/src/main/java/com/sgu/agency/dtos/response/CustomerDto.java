package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class CustomerDto {
    private String id;
    private String name;
    private Date birthDate;
    private String phone;
    private String email;
    private String password;
    private Boolean isValid;
    private RoleDto role;
    private Date createdDate;
    private Date updatedDate;
}
