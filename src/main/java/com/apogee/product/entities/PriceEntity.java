package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prices")
public class PriceEntity {

    @Id
    // add generatedValue with strategy to use GenerationType.IDENTITY for auto-incrementing primary key for MySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "price_id")
    private Long priceId;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;


}