package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.HistoricalRatesModel;
import com.allobank.allo_backend_test.finance.model.dto.HistoricalRatesDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdHandlerTest {

    @Mock private DataSourceClient client;
    @Mock private FinanceRepository repository;

    @InjectMocks private HistoricalIdrUsdHandler handler;

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("historical_idr_usd", handler.resourceType());
    }

    @Test
    void shouldFetchAndStore() {
        HistoricalRatesDto dto = new HistoricalRatesDto(
                1.0, "IDR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5),
                Map.of("2024-01-01", Map.of("USD", 0.000064)));

        when(client.getWithParams(anyString(), anyMap(), eq(HistoricalRatesDto.class))).thenReturn(dto);

        HistoricalRatesModel result = (HistoricalRatesModel) handler.fetch();

        assertNotNull(result);
        assertEquals("IDR", result.base());
    }

    @Test
    void shouldGetFromRepository() {
        HistoricalRatesModel model = new HistoricalRatesModel(
                1.0, "IDR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5),
                Map.of());
        when(repository.getData()).thenReturn(Map.of("historical_idr_usd", model));
        when(repository.get("historical_idr_usd")).thenReturn(model);

        assertEquals(model, handler.get());
    }
}