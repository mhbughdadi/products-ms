package com.apogee.product.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllSkusResponseDto extends SuccessfulResponse {

    private List<SkuOutputDto> skus;
}
