package com.apogee.product.backingservice;

import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.utilities.Mapper;
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

    public CurrencyResponseDto addCurrency(CurrencyDto currencyDto) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.saveCurrency(Mapper.map(currencyDto, Currency.class));

        response.setCurrency(Mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto updateCurrency(CurrencyDto currencyDto) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.UpdateCurrency(Mapper.map(currencyDto, Currency.class));

        response.setCurrency(Mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto deleteCurrency(Long currencyId) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.deleteCurrency(currencyId);

        response.setCurrency(Mapper.map(savedCurrency, CurrencyDto.class));

        return response;
    }

    public AllCurrenciesResponseDto getAllCurrencies() throws Exception {

        AllCurrenciesResponseDto response = new AllCurrenciesResponseDto();

        List<Currency> allCurrencies = this.currencyService.findAllCurrencies();
        List<CurrencyDto> allCurrenciesDto = transformCollection(allCurrencies, CurrencyDto.class);

        response.setCurrencies(allCurrenciesDto);

        return response;
    }

    public CurrencyResponseDto getCurrencyById(Long currencyId) throws Exception {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.findCurrency(currencyId);

        response.setCurrency(Mapper.map(savedCurrency, CurrencyDto.class));

        return response;

    }


}
