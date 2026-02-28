package com.apogee.product.models;

import com.apogee.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Sku {

    private Long id;
    private String skuCode;
    private String titleAr;
    private String titleEn;
    private String subTitleAr;
    private String subTitleEn;
    private String descriptionAr;
    private String descriptionEn;
    private Double price;
    private Boolean active;
    private ProductStatus status;
    private String longDescriptionAr;
    private String longDescriptionEn;
    private Date availableFrom;
    private Date availableUntil;
    private Boolean digital;
    private Boolean shippable;
    private Integer weightGrams;
    private String dimensions;
    private Long stockQuantity;
    private String barcode;
    private String attributes;
    private Long productId;
    private List<Benefit> benefits;
    private List<Tag> tags;
}
