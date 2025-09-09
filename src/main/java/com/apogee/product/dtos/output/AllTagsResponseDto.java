package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.TagDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllTagsResponseDto extends SuccessfulResponse {
    List<TagDto> tags;
}
