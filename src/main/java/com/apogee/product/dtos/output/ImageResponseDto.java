package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.ImageDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageResponseDto extends SuccessfulResponse {
    ImageDto image;
}
