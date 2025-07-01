package com.apogee.product.services.impl;

import com.apogee.product.entities.CurrencyEntity;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Currency;
import com.apogee.product.repositories.CurrencyRepository;
import com.apogee.product.services.CurrencyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private CurrencyRepository currencyRepository;


    @Override
    public Currency saveCurrency(Currency currency) throws Exception {

        var currencyEntity = mapper.map(currency, CurrencyEntity.class);

        var savedEntity = currencyRepository.save(currencyEntity);

        return mapper.map(savedEntity, Currency.class);

    }
}
