package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Category {

    private Long id;
    private String descriptionAr;
    private String descriptionEn;
    private String nameAr;
    private String nameEn;
    private String code;
    private boolean active;
    private List<Category> subCategories;
    private Long parentId;
    private List<Tag> tags;

}
