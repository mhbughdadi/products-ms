package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.TagDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Setter
@Getter
public class AllTagsResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    List<TagDto> tags;
}
