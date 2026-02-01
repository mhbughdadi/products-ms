package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.CurrencyDto;
import com.apogee.product.dtos.output.AllCurrenciesResponseDto;
import com.apogee.product.dtos.output.CurrencyResponseDto;
import com.apogee.product.models.Currency;
import com.apogee.product.services.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrenciesBackingServiceTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrenciesBackingService backingService;

    @Test
    void add_update_delete_getAll_getById() throws Exception {
        CurrencyDto dto = new CurrencyDto();
        dto.setCode("USD");

        Currency model = new Currency();
        model.setCurrencyId(10L);
        model.setCode("USD");

        when(currencyService.saveCurrency(any(Currency.class))).thenReturn(model);
        when(currencyService.UpdateCurrency(any(Currency.class))).thenReturn(model);
        when(currencyService.deleteCurrency(10L)).thenReturn(model);
        when(currencyService.findAllCurrencies()).thenReturn(List.of(model));
        when(currencyService.findCurrency(10L)).thenReturn(model);

        CurrencyResponseDto added = backingService.addCurrency(dto);
        assertEquals("USD", added.getCurrency().getCode());

        CurrencyResponseDto updated = backingService.updateCurrency(dto);
        assertEquals(10L, updated.getCurrency().getCurrencyId());

        CurrencyResponseDto deleted = backingService.deleteCurrency(10L);
        assertEquals(10L, deleted.getCurrency().getCurrencyId());

        AllCurrenciesResponseDto all = backingService.getAllCurrencies();
        assertEquals(1, all.getCurrencies().size());

        CurrencyResponseDto byId = backingService.getCurrencyById(10L);
        assertEquals(10L, byId.getCurrency().getCurrencyId());
    }

    @Test
    void getAllCurrencies_returnsEmpty_whenNone() throws Exception {
        when(currencyService.findAllCurrencies()).thenReturn(List.of());
        AllCurrenciesResponseDto all = backingService.getAllCurrencies();
        assertNotNull(all);
        assertTrue(all.getCurrencies().isEmpty());
    }

    @Test
    void getCurrencyById_propagatesException() throws Exception {
        when(currencyService.findCurrency(99L)).thenThrow(new RuntimeException("no currency"));
        assertThrows(RuntimeException.class, () -> backingService.getCurrencyById(99L));
    }
}
