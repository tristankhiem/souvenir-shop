package com.sgu.agency.dtos.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SizeDto {
    private String id;
    @NotEmpty(message = "Tên của kích cỡ không được để trống")
    private String name;
}
