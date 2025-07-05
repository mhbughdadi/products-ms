package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.CurrencyDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyResponseDto extends SuccessfulResponse {
    CurrencyDto currency;
}
