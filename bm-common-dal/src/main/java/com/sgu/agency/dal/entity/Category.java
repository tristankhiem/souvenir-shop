package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    private String id;
    @Column
    private String name;
}
