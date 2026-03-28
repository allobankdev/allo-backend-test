package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.LatestRatesModel;
import com.allobank.allo_backend_test.finance.model.dto.LatestRateDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.SpreadService;
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
class LatestIdrRatesHandlerTest {

    @Mock private DataSourceClient client;
    @Mock private SpreadService spreadService;
    @Mock private FinanceRepository repository;

    @InjectMocks private LatestIdrRatesHandler handler;

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("latest_idr_rates", handler.resourceType());
    }

    @Test
    void shouldFetchAndStoreWithSpread() {
        LatestRateDto dto = new LatestRateDto(1.0, "IDR", LocalDate.now(), Map.of("USD", 0.000064));
        when(client.getWithParams(anyString(), anyMap(), eq(LatestRateDto.class))).thenReturn(dto);
        when(spreadService.calculateSpread(anyDouble())).thenReturn(15740.9375);

        LatestRatesModel result = (LatestRatesModel) handler.fetch();

        assertNotNull(result);
        assertEquals("IDR", result.base());
        assertEquals(15740.9375, result.usdBuySpreadIdr());
    }

    @Test
    void shouldGetFromRepository() {
        LatestRatesModel model = new LatestRatesModel(
                1.0, "IDR", LocalDate.now(), Map.of("USD", 0.000064), 15740.9375);
        when(repository.getData()).thenReturn(Map.of("latest_idr_rates", model));
        when(repository.get("latest_idr_rates")).thenReturn(model);

        assertEquals(model, handler.get());
    }
}