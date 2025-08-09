package com.apogee.product.dtos.inputs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDto {

    private String descriptionAr;
    private String descriptionEn;
    private String nameAr;
    private String nameEn;
    private String code;
    private boolean active;

}
