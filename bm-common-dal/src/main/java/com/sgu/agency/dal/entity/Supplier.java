package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String address;
}
