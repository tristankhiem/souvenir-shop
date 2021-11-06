package com.sgu.agency.dal.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_category")
public class SubCategory {
    @Id
    private String id;
    @Column
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
