package com.apogee.product.dtos.output;

import com.apogee.product.dtos.inputs.CurrencyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllCurrenciesResponseDto extends SuccessfulResponse {
    List<CurrencyDto> currencies;
}
