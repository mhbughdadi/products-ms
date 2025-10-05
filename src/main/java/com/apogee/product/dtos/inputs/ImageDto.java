package com.apogee.product.dtos.inputs;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ImageDto {

    private Long id;

    private String url;

    private String altTextEn;

    private String altTextAr;

    private Date createdAt;

    private boolean assigned;

    private boolean isActive;

    private int sortOrder;

    private String type;

}
