package com.allobank.allo_backend_test.finance.repository;

import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceRepositoryTest {

    @Mock
    private FinanceRepository repository;

    @Test
    void shouldStoreCorreftly() {
        CurrenciesModel model = new CurrenciesModel(Map.of("USD", "dolar"));
        when(repository.get("USD")).thenReturn(model);

        assertEquals(model, repository.get("USD"));
        verify(repository).get("USD");
    }

    @Test
    void shouldNullIfNotExists() {
        when(repository.get("unknown")).thenReturn(null);

        assertNull(repository.get("unknown"));
        verify(repository).get("unknown");
    }

    @Test
    void shouldReturnAllData() {
        CurrenciesModel model = new CurrenciesModel(Map.of("USD", "dolar"));
        when(repository.getData()).thenReturn(Map.of("supported_currencies", model));

        assertEquals(1, repository.getData().size());
        verify(repository).getData();
    }
}