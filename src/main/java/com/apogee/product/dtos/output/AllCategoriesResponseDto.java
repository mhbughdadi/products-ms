package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.CategoryDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Setter
@Getter
public class AllCategoriesResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    List<CategoryDto> categories;
}
