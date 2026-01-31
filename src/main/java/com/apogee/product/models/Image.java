package com.apogee.product.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Image {

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
