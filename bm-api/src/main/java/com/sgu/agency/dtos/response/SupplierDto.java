package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SupplierDto {
    private String id;
    @NotEmpty(message = "Tên của màu sắc không được để trống")
    private String name;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
}
