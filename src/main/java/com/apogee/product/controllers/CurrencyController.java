package com.apogee.product.controllers;

import com.apogee.product.backingService.ProductsBackingService;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.dtos.output.AddCurrencyResponseDto;
import com.apogee.product.dtos.output.AddProductResponseDto;
import com.apogee.product.dtos.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    @Autowired
    private ProductsBackingService productsBackingService;

    @PostMapping("/currencies")
    public ResponseEntity<Response> addCurrency(@RequestBody CurrencyDto currency) throws Exception {

        AddCurrencyResponseDto response = productsBackingService.addCurrency(currency);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
