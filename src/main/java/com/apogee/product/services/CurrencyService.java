package com.apogee.product.services;

import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Currency;

import java.util.List;

public interface CurrencyService {

    /**
     * Save currency
     * @param currency model
     * @return saved currency
     * @throws MapperException mapping failure
     */
    Currency saveCurrency(Currency currency) throws MapperException;

    /**
     * Find currency
     * @param currencyId id
     * @return currency
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Currency findCurrency(Long currencyId) throws MapperException, RecordNotFoundException;

    /**
     * Return all currencies
     * @return list
     * @throws MapperException mapping failure
     */
    List<Currency> findAllCurrencies() throws MapperException;

    /**
     * Update currency
     * @param currency model
     * @return updated currency
     * @throws MapperException mapping failure
     */
    Currency UpdateCurrency(Currency currency) throws MapperException;

    /**
     * Delete currency
     * @param currencyId id
     * @return deleted currency
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Currency deleteCurrency(Long currencyId) throws MapperException, RecordNotFoundException;

}
