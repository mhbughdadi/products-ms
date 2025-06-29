package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddProductResponseDto extends SuccessfulResponse {

    private ProductOutputDto product;
}
