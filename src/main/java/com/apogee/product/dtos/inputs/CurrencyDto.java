package com.apogee.product.dtos.inputs;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class CurrencyDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String symbol;
    private String nameAR;
    private double exchangeRate;
    @Nullable
    private Long currencyId;

}
