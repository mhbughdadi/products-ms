package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.TagDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class TagResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    TagDto tag;
}
