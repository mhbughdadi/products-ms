package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Image {

    private Long imageId;
    private String largeScreen;
    private String smallScreen;
    private String mediumScreen;
    private Product product;
}
