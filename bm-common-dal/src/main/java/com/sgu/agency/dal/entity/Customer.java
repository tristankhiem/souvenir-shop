package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    private String id;
    @Column
    private String name;
    @Column(name="birth_date")
    private Date birthDate;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name="is_valid")
    private Boolean isValid;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    protected void onCreate() { createdDate = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedDate = new Date(); }
}
