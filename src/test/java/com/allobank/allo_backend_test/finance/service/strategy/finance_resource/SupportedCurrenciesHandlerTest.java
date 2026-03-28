package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import com.allobank.allo_backend_test.finance.model.dto.CurrenciesDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesHandlerTest {

    @Mock private DataSourceClient client;
    @Mock private FinanceRepository repository;

    @InjectMocks private SupportedCurrenciesHandler handler;

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("supported_currencies", handler.resourceType());
    }

    @Test
    void shouldFetchAndStore() {
        CurrenciesDto dto = new CurrenciesDto();
        dto.put("USD", "dollar");
        dto.put("IDR", "rupiah");
        when(client.get(anyString(), eq(CurrenciesDto.class))).thenReturn(dto);

        CurrenciesModel result = (CurrenciesModel) handler.fetch();

        assertNotNull(result);
        assertEquals(2, result.currencies().size());
    }

    @Test
    void shouldGetFromRepository() {
        CurrenciesModel model = new CurrenciesModel(
                java.util.Map.of("usd", "dollar"));
        when(repository.getData()).thenReturn(Map.of("supported_currencies", model));
        when(repository.get("supported_currencies")).thenReturn(model);

        assertEquals(model, handler.get());
    }
}