package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "price_id")
    private Long priceId;

    @Column(name = "price")
    private Double price;

    @OneToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    private CurrencyEntity currency;


}