package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.ImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllImagesResponseDto extends SuccessfulResponse {

    private List<ImageDto> images;
}
