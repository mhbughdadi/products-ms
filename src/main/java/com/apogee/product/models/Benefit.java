package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Benefit {

    private Long id;

    private String kayAr;

    private String keyEn;

    private String valueAr;

    private String valueEn;

    private String imageUrl;

    private Long skuId;
}
