package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "benefits")
public class BenefitEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "key_ar")
    private String kayAr;

    @Column(name = "key_en")
    private String keyEn;

    @Column(name = "value_ar")
    private String valueAr;

    @Column(name = "value_en")
    private String valueEn;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "sku_id", referencedColumnName = "id")
    private SkuEntity sku;

}
