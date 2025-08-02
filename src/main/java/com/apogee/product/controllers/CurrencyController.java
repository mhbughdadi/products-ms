package com.apogee.product.controllers;

import com.apogee.product.backingService.CurrenciesBackingService;
import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.dtos.output.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Add a new currency", description = "This endpoint allows you to add a new currency to the system.")
    public ResponseEntity<Response> addCurrency(@RequestBody CurrencyDto currency) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.addCurrency(currency);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/currencies")
    @Operation(summary = "Update an existing currency", description = "This endpoint allows you to update an existing currency in the system.")
    public ResponseEntity<Response> updateCurrency(@RequestBody CurrencyDto currency) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.updateCurrency(currency);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    @Operation(summary = "Get all currencies", description = "This endpoint retrieves all currencies available in the system.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AllCurrenciesResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Response.class)
                    )
            )})
    public ResponseEntity<AllCurrenciesResponseDto> getAllCurrencies() throws Exception {

        AllCurrenciesResponseDto response = currenciesBackingService.getAllCurrencies();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/currencies/{currencyId}")
    @Operation(summary = "Delete a currency", description = "This endpoint allows you to delete a currency by its ID.")
    public ResponseEntity<Response> deleteCurrency(@PathVariable("currencyId") Long currencyId) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.deleteCurrency(currencyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currencies/{currencyId}")
    @Operation(summary = "Get a currency by ID", description = "This endpoint retrieves a specific currency by its ID.")
    public ResponseEntity<Response> getCurrency(@PathVariable("currencyId") Long currencyId) throws Exception {

        CurrencyResponseDto response = currenciesBackingService.getCurrencyById(currencyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
