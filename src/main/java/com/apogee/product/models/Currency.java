package com.apogee.product.models;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Currency {

    private String code;
    private String name;
    private String symbol;
    private String nameAR;
    private double exchangeRate;
    private Long currencyId;


}
