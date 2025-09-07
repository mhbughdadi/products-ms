package com.apogee.product.services.impl;

import com.apogee.product.entities.CurrencyEntity;
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

import static com.apogee.product.utilities.Utilities.transformCollection;

@Transactional
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;


    @Override
    public Currency saveCurrency(Currency currency) throws Exception {

        var currencyEntity = Mapper.map(currency, CurrencyEntity.class);

        var savedEntity = currencyRepository.save(currencyEntity);

        return Mapper.map(savedEntity, Currency.class);

    }

    @Override
    public Currency findCurrency(Long currencyId) throws Exception {

        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(currencyId);

        CurrencyEntity foundCurrencyEntity = currencyOpt.orElseThrow(() -> new RecordNotFoundException("record.not.found", currencyId));

        return Mapper.map(foundCurrencyEntity, Currency.class);
    }

    @Override
    public List<Currency> findAllCurrencies() throws Exception {

        List<CurrencyEntity> foundCurrencies = this.currencyRepository.findAll();

        return transformCollection(foundCurrencies, Currency.class);
    }

    @Override
    public Currency UpdateCurrency(Currency currency) throws Exception {

        CurrencyEntity updatedCurrency = this.currencyRepository.save(Mapper.map(currency, CurrencyEntity.class));

        return Mapper.map(updatedCurrency, Currency.class);
    }

    @Override
    public Currency deleteCurrency(Long currencyId) throws Exception {

        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(currencyId);

        CurrencyEntity toBeDeletedEntity = currencyOpt.orElseThrow(() -> new RecordNotFoundException("record.not.found", currencyId));

        this.currencyRepository.deleteById(currencyId);

        return Mapper.map(toBeDeletedEntity, Currency.class);
    }
}
