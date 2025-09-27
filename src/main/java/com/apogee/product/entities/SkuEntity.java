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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "skus")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("sku")
public class SkuEntity extends ParentItemEntity {

    @Column(name = "sku_code")
    private String skuCode;

    @Column(name = "title_ar")
    private String titleAr;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "subtitle_ar")
    private String subTitleAr;

    @Column(name = "subtitle_en")
    private String subTitleEn;

    @Column(name = "description_ar")
    private String descriptionAr;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "price")
    private Double price;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "long_description_ar")
    private String longDescriptionAr;

    @Column(name = "long_description_en")
    private String longDescriptionEn;

    @Column(name = "available_from")
    private Date availableFrom;

    @Column(name = "available_until")
    private Date availableUntil;

    @Column(name = "is_digital")
    private Boolean digital;

    @Column(name = "is_shippable")
    private Boolean shippable;

    @Column(name = "weight_grams")
    private Integer weightGrams;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "attributes", columnDefinition = "json")
    private String attributes;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

    @OneToMany(mappedBy = "sku")
    List<BenefitEntity> benefits;

}
