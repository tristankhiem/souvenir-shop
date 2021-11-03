package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    private String code;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "feature_key")
    private String featureKey;
    @Column(name = "global_admin_only")
    private boolean globalAdminOnly;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    protected void onCreate() { createdDate = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedDate = new Date(); }
}
