package com.apogee.product.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BenefitDto {

    private Long id;

    private String kayAr;

    private String keyEn;

    private String valueAr;

    private String valueEn;

    private String imageUrl;

    private Long skuId;
}
