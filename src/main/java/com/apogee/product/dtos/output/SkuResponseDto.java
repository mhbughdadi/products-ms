package com.apogee.product.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkuResponseDto extends SuccessfulResponse {

    private SkuOutputDto sku;
}
