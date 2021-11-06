package com.sgu.agency.dal.entity;

import com.sgu.agency.common.enums.BlockStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "employees")
public class Employees {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name="birth_date")
    private Date birthDate;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    protected void onCreate() { createdDate = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedDate = new Date(); }
}
