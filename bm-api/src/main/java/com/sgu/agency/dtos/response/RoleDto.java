package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDto {
    private String id;
    private String name;
    private Date createdDate;
    private Date updatedDate;
}
