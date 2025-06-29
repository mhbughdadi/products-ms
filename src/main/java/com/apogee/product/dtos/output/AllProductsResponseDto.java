package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.ProductDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllProductsResponseDto extends SuccessfulResponse {

    private List<ProductOutputDto> products;
}