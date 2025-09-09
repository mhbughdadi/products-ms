package com.apogee.product.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {

    private Long id;
    private String name;
    private String description;
    private String descriptionAr;

}
