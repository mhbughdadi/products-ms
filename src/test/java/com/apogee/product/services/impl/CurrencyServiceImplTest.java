package com.apogee.product.services.impl;

import com.apogee.product.entities.CurrencyEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Currency;
import com.apogee.product.repositories.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private CurrencyEntity buildCurrencyEntity(Long id, String code) {
        CurrencyEntity e = new CurrencyEntity();
        e.setCurrencyId(id);
        if (code != null) e.setCode(code);
        return e;
    }

    @Test
    public void saveCurrency_savesAndReturns() throws Exception {
        Currency c = new Currency();
        c.setCode("USD");

        CurrencyEntity saved = buildCurrencyEntity(1L, "USD");

        when(currencyRepository.save(any(CurrencyEntity.class))).thenReturn(saved);

        Currency out = currencyService.saveCurrency(c);
        assertNotNull(out);
        assertEquals(1L, out.getCurrencyId());
    }

    @Test
    public void findCurrency_returnsWhenFound() throws Exception {
        CurrencyEntity e = buildCurrencyEntity(2L, null);
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(e));

        Currency out = currencyService.findCurrency(2L);
        assertNotNull(out);
        assertEquals(2L, out.getCurrencyId());
    }

    @Test
    public void findCurrency_throwsWhenNotFound() {
        when(currencyRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> currencyService.findCurrency(3L));
    }

    @Test
    public void findAllCurrencies_returnsList() throws Exception {
        CurrencyEntity e = new CurrencyEntity();
        e.setCurrencyId(4L);
        when(currencyRepository.findAll()).thenReturn(List.of(e));

        List<Currency> out = currencyService.findAllCurrencies();
        assertEquals(1, out.size());
    }

    @Test
    public void updateCurrency_savesAndReturns() throws Exception {
        Currency c = new Currency();
        c.setCurrencyId(5L);
        c.setCode("EUR");

        when(currencyRepository.save(any(CurrencyEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Currency out = currencyService.UpdateCurrency(c);
        assertNotNull(out);
    }

    @Test
    public void deleteCurrency_deletesWhenFound() throws Exception {
        CurrencyEntity e = new CurrencyEntity();
        e.setCurrencyId(6L);
        when(currencyRepository.findById(6L)).thenReturn(Optional.of(e));

        Currency out = currencyService.deleteCurrency(6L);
        assertNotNull(out);
        assertEquals(6L, out.getCurrencyId());
        verify(currencyRepository).deleteById(6L);
    }

    @Test
    public void deleteCurrency_throwsWhenNotFound() {
        when(currencyRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> currencyService.deleteCurrency(7L));
    }
}
