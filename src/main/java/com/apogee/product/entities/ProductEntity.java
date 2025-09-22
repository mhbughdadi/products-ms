package com.apogee.product.entities;

import com.apogee.product.enums.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "products")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("product")
public class ProductEntity extends ParentItemEntity {

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAR;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "description_ar")
    private String descriptionAr;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "brand")
    private String brand;

    @Column(name = "manufacturer_code")
    private String manufacturerCode;

    @Column(name = "warranty_info_en")
    private String warrantyInfoEn;

    @Column(name = "warranty_info_ar")
    private String warrantyInfoAr;

    @Column(name = "return_policy_ar")
    private String returnPolicyAr;

    @Column(name = "return_policy_en")
    private String returnPolicyEn;

    @Column(name = "available_from")
    private Date availableFrom;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "default_currency")
    private String defaultCurrency;

    @Column(name = "is_featured")
    private Boolean featured;

    @Column(name = "seo_title_en")
    private String seoTitleEn;

    @Column(name = "seo_title_ar")
    private String seoTitleAr;

    @Column(name = "seo_description_en")
    private String seoDescriptionEn;

    @Column(name = "seo_description_ar")
    private String seoDescriptionAr;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "rating_avg")
    private Double ratingAvg;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "code")
    private String code;

    @ManyToMany
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

}
