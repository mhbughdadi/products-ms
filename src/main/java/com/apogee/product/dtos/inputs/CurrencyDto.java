package com.apogee.product.dtos.inputs;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyDto {

    private String code;
    private String name;
    private String symbol;
    private String nameAR;
    private double exchangeRate;
    @Nullable
    private Long currencyId;

}
