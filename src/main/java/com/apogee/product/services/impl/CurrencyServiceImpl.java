package com.apogee.product.services.impl;

import com.apogee.product.entities.CurrencyEntity;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Currency;
import com.apogee.product.repositories.CurrencyRepository;
import com.apogee.product.services.CurrencyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.apogee.product.constants.ProductsConstant.ERROR_RECORD_NOT_FOUND;
import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Transactional
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;


    @Override
    public Currency saveCurrency(Currency currency) throws MapperException {

        var currencyEntity = transform(currency, CurrencyEntity.class);

        var savedEntity = currencyRepository.save(currencyEntity);

        return transform(savedEntity, Currency.class);

    }

    @Override
    public Currency findCurrency(Long currencyId) throws MapperException, RecordNotFoundException {

        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(currencyId);

        CurrencyEntity foundCurrencyEntity = currencyOpt.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, currencyId));

        return transform(foundCurrencyEntity, Currency.class);
    }

    @Override
    public List<Currency> findAllCurrencies() throws MapperException {

        List<CurrencyEntity> foundCurrencies = this.currencyRepository.findAll();

        return transformCollection(foundCurrencies, Currency.class);
    }

    @Override
    public Currency UpdateCurrency(Currency currency) throws MapperException {

        CurrencyEntity updatedCurrency = this.currencyRepository.save(transform(currency, CurrencyEntity.class));

        return transform(updatedCurrency, Currency.class);
    }

    @Override
    public Currency deleteCurrency(Long currencyId) throws MapperException, RecordNotFoundException {

        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(currencyId);

        CurrencyEntity toBeDeletedEntity = currencyOpt.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, currencyId));

        this.currencyRepository.deleteById(currencyId);

        return transform(toBeDeletedEntity, Currency.class);
    }
}
