package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "description_ar")
    private String descriptionAr;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(mappedBy = "tags")
    private List<ParentItemEntity> items;
}
