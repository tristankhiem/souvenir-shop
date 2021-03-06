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
}
