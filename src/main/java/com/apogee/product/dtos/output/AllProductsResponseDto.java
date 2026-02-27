package com.apogee.product.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Setter
@Getter
public class AllProductsResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    private List<ProductOutputDto> products;
}