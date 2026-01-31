package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.CategoryDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponseDto extends SuccessfulResponse {
    CategoryDto category;
}
