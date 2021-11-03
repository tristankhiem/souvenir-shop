package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "agency")
public class Agency {
    @Id
    private String id;
    @Column(name="company_id")
    private String companyId;
    @Column
    private String name;
    @Column
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name="org_code")
    private String orgCode;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    protected void onCreate() { createdDate = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedDate = new Date(); }
}
