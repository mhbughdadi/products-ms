package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("category")
public class CategoryEntity extends ParentItemEntity {

    @Column(name = "description_ar")
    private String descriptionAr;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private boolean active;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToMany(mappedBy = "categories")
    private Set<ProductEntity> products;

}