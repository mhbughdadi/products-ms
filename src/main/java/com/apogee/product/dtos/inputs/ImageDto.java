package com.apogee.product.dtos.inputs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ImageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
