package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.TagDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagResponseDto extends SuccessfulResponse {
    TagDto tag;
}
