package com.apogee.product.services;

import com.apogee.product.models.Currency;

import java.util.List;

public interface CurrencyService {

    Currency saveCurrency(Currency currency) throws Exception;

    Currency findCurrency(Long currencyId) throws Exception;

    List<Currency> findAllCurrencies() throws Exception;

    Currency UpdateCurrency(Currency currency) throws Exception;

    Currency deleteCurrency(Long currencyId) throws Exception;

}
