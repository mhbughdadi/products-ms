package com.apogee.product.backingService;

import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.dtos.output.ProductOutputDto;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Currency;
import com.apogee.product.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CurrenciesBackingService {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private Mapper mapper;

    public CurrencyResponseDto addCurrency(CurrencyDto currencyDto) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.saveCurrency(this.mapper.map(currencyDto, Currency.class));

        response.setCurrency(this.mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto updateCurrency(CurrencyDto currencyDto) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.UpdateCurrency(this.mapper.map(currencyDto, Currency.class));

        response.setCurrency(this.mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto deleteCurrency(Long currencyId) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.deleteCurrency(currencyId);

        response.setCurrency(this.mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public AllCurrenciesResponseDto getAllCurrencies() throws Exception {

        AllCurrenciesResponseDto response = new AllCurrenciesResponseDto();

        List<Currency> allCurrencies = this.currencyService.findAllCurrencies();
        List<CurrencyDto> allCurrenciesDto = transformCollection(allCurrencies, (currency) -> this.mapper.map(currency, CurrencyDto.class));

        response.setCurrencies(allCurrenciesDto);

        return response;
    }

    public CurrencyResponseDto getCurrencyById(Long currencyId) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.findCurrency(currencyId);

        response.setCurrency(this.mapper.map(savedCurrency, CurrencyDto.class));

        return response;

    }


}
