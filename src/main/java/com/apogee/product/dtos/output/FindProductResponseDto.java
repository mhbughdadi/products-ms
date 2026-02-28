package com.apogee.product.dtos.output;


import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class FindProductResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    ProductOutputDto product;
}
