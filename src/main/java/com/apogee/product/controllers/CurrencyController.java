package com.apogee.product.controllers;

import com.apogee.product.backingService.CurrenciesBackingService;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.dtos.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    @Autowired
    private CurrenciesBackingService currenciesBackingService;

    @PostMapping("/currencies")
    public ResponseEntity<Response> addCurrency(@RequestBody CurrencyDto currency) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.addCurrency(currency);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/currencies")
    public ResponseEntity<Response> updateCurrency(@RequestBody CurrencyDto currency) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.updateCurrency(currency);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<Response> getAllCurrencies() throws Exception {

        AllCurrenciesResponseDto response = currenciesBackingService.getAllCurrencies();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/currencies/{currencyId}")
    public ResponseEntity<Response> deleteCurrency(@PathVariable("currencyId") Long currencyId) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.deleteCurrency(currencyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currencies/{currencyId}")
    public ResponseEntity<Response> getCurrency(@PathVariable("currencyId") Long currencyId) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.getCurrencyById(currencyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
