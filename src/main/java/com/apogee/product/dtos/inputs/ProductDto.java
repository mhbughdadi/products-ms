package com.apogee.product.dtos.inputs;

import com.apogee.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ProductDto{

    private Long id;
    private String nameEn;
    private String nameAR;
    private String descriptionEn;
    private String descriptionAr;
    private Boolean active;
    private String brand;
    private String manufacturerCode;
    private String warrantyInfoEn;
    private String warrantyInfoAr;
    private String returnPolicyAr;
    private String returnPolicyEn;
    private Date availableFrom;
    private ProductStatus status;
    private String defaultCurrency;
    private Boolean featured;
    private String seoTitleEn;
    private String seoTitleAr;
    private String seoDescriptionEn;
    private String seoDescriptionAr;
    private Integer viewCount;
    private Double ratingAvg;
    private Long reviewCount;
    private String code;
}
