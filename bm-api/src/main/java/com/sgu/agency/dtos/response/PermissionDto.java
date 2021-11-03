package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionDto {
    private String code;
    private String name;
    private String description;
    private String featureKey;
    private boolean globalAdminOnly;
    private Date createdDate;
    private Date updatedDate;
}
