package com.apogee.product.backingservice;

import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Currency;
import com.apogee.product.services.CurrencyService;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CurrenciesBackingService {

    @Autowired
    private CurrencyService currencyService;

    public CurrencyResponseDto addCurrency(CurrencyDto currencyDto) throws MapperException {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.saveCurrency(transform(currencyDto, Currency.class));

        response.setCurrency(transform(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto updateCurrency(CurrencyDto currencyDto) throws MapperException {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.UpdateCurrency(transform(currencyDto, Currency.class));

        response.setCurrency(transform(savedCurrency, CurrencyDto.class));

        return response;
    }

    public CurrencyResponseDto deleteCurrency(Long currencyId) throws MapperException, RecordNotFoundException {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.deleteCurrency(currencyId);

        response.setCurrency(transform(savedCurrency, CurrencyDto.class));

        return response;
    }

    public AllCurrenciesResponseDto getAllCurrencies() throws MapperException {

        AllCurrenciesResponseDto response = new AllCurrenciesResponseDto();

        List<Currency> allCurrencies = this.currencyService.findAllCurrencies();
        List<CurrencyDto> allCurrenciesDto = transformCollection(allCurrencies, CurrencyDto.class);

        response.setCurrencies(allCurrenciesDto);

        return response;
    }

    public CurrencyResponseDto getCurrencyById(Long currencyId) throws MapperException, RecordNotFoundException {

        CurrencyResponseDto response = new CurrencyResponseDto();

        Currency savedCurrency = this.currencyService.findCurrency(currencyId);

        response.setCurrency(transform(savedCurrency, CurrencyDto.class));

        return response;

    }


}
