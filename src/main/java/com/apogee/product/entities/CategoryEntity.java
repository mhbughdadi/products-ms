package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("category")
public class CategoryEntity extends AuditableItem {

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

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<CategoryEntity> subCategories;

    @ManyToMany(mappedBy = "categories")
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "category")
    private List<CategoryTagEntity> tags;

}