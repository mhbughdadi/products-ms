package com.apogee.product.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private Long imageId;
    private String smallScreen;
    private String mediumScreen;
    private String largeScreen;

}
