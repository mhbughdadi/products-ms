package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.BenefitDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class BenefitResponseDto extends SuccessfulResponse {

    @Serial
    private static final long serialVersionUID = 1L;
    private BenefitDto benefit;
}
