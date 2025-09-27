package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.BenefitDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BenefitResponseDto extends SuccessfulResponse {

    private BenefitDto benefit;
}
