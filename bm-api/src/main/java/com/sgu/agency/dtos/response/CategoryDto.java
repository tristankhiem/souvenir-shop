package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class CategoryDto {
    private String id;
    @NotEmpty(message = "Tên của nhóm hàng hóa không được để trống")
    private String name;
}
