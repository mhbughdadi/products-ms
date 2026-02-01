package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.ProductDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class AddProductResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    private ProductOutputDto product;
}
