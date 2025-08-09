package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Category {

    private String descriptionAr;
    private String descriptionEn;
    private String nameAr;
    private String nameEn;
    private String code;
    private boolean active;

}
