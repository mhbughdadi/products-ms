package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(name= "name")
    private String name;
    @Column(name= "name_ar")
    private String nameAR;
    @Column(name= "description")
    private String description;
    @Column(name= "description_ar")
    private String descriptionAr;
    @Column(name= "list_price")
    private Double listPrice;
    @Column(name= "vat")
    private Boolean vat;
    @Column(name= "short_description")
    private String shortDescription;
    @Column(name= "short_description_ar")
    private String shortDescriptionAr;

    @Column(name= "expiration_date")
    private Date expirationDate;
    @Column(name= "manufacturing_date")
    private String manufacturingDate;
    @Column(name= "sku")
    private String sku;
    @Column(name= "quantity")
    private Long quantity;
    @Column(name= "status")
    private Boolean status;
    @Column(name= "created_at")
    private Date createdAt;
    @Column(name= "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @OneToOne
    @JoinColumn(name = "price_id", referencedColumnName = "price_id", insertable = false)
    private PriceEntity price;

    @OneToOne
    @JoinColumn(name = "old_price_id", referencedColumnName = "price_id", insertable = false)
    private PriceEntity oldPrice;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ImageEntity> images;

}
