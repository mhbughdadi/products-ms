package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.BenefitDto;
import com.apogee.product.enums.ProductStatus;
import com.apogee.product.models.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuOutputDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    private List<BenefitDto> benefits;
    private List<Tag> tags;
}
