package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.CurrencyDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCurrencyResponseDto extends SuccessfulResponse {
    private CurrencyDto currency;
}
