package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private FinanceService financeService;

    @Test
    void shouldFindCorrectly() {
        CurrenciesModel model = new CurrenciesModel(Map.of("USD", "dolar"));
        when(financeService.findByResourceType("USD")).thenReturn(model);

        assertEquals(model, financeService.findByResourceType("USD"));
        verify(financeService).findByResourceType("USD");
    }

    @Test
    void shouldNullIfNotExists() {
        when(financeService.findByResourceType("unknown")).thenReturn(null);

        assertNull(financeService.findByResourceType("unknown"));
        verify(financeService).findByResourceType("unknown");
    }
}