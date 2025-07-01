package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long currencyId;
    @Column(name = "name")
    private String name;
    @Column(name = "name_ar")
    private String nameAR;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "code")
    private String code;
    @Column(name = "exchange_rate")
    private double exchangeRate;

    @OneToOne(mappedBy = "currency")
    private PriceEntity price;
}