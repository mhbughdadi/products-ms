package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.BenefitDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllBenefitResponseDto extends SuccessfulResponse {

    private List<BenefitDto> benefits;
}
