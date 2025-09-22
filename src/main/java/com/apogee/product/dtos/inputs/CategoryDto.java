package com.apogee.product.dtos.inputs;

import com.apogee.product.models.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {

    private String descriptionAr;
    private String descriptionEn;
    private String nameAr;
    private String nameEn;
    private String code;
    private Long id;
    private boolean active;
    private ArrayList<CategoryDto> subCategories;
    private Long parentId;
    private List<Tag> tags;

}
