package com.sgu.agency.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class AgencyDto {
    private String id;
    private String companyId;
    private String name;
    private String address;
    private String phone;
    private String orgCode;
    private Date createdDate;
    private Date updatedDate;
}
