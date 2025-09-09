package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Tag {

    private Long id;
    private String name;
    private String description;
    private String descriptionAR;
    private Date createdAt;
}
