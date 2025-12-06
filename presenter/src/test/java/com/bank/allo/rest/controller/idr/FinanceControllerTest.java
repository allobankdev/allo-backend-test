package com.bank.allo.rest.controller.idr;

import com.bank.allo.domain.idr.HistoricalRates;
import com.bank.allo.domain.idr.LatestRates;
import com.bank.allo.domain.idr.SupportedCurrencies;
import com.bank.allo.exception.BadRequestException;
import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.rest.entity.ApiResponse;
import com.bank.allo.rest.entity.historical.HistoricalRatesResponse;
import com.bank.allo.rest.entity.latest.LatestRatesResponse;
import com.bank.allo.rest.entity.supported.SupportedCurrenciesResponse;
import com.bank.allo.rest.mapper.FinanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FinanceControllerTest {

    @Mock
    DataStore dataStore;
    @Mock
    FinanceMapper mapper;

    // system under test
    FinanceController controller;

    @BeforeEach
    void setUp() {
        controller = new FinanceController(dataStore, mapper);
    }

    @Test
    void getFinanceData_returnsLatestMapped() {
        LatestRates latestDomain = LatestRates.builder()
                .base("IDR")
                .date("2025-12-04")
                .rates(Map.of("USD", 0.00006))
                .usdBuySpreadIdr(16829.166)
                .spreadFactor(0.00975)
                .build();

        LatestRatesResponse mapped = LatestRatesResponse.builder()
                .base("IDR")
                .date("2025-12-04")
                .rates(Map.of("USD", 0.00006))
                .usdBuySpreadIdr(16829.166)
                .spreadFactor(0.00975)
                .build();

        when(dataStore.get("latest_idr_rates")).thenReturn(latestDomain);
        when(mapper.toLatestRatesResponse(latestDomain)).thenReturn(mapped);

        ApiResponse<?> resp = controller.getFinanceData("latest_idr_rates");

        assertNotNull(resp);
        assertEquals(200, resp.getCode());
        assertTrue(resp.getMessage().contains("latest_idr_rates"));
        assertSame(mapped, resp.getData());
        verify(mapper).toLatestRatesResponse(latestDomain);
    }

    @Test
    void getFinanceData_returnsHistoricalMapped() {
        HistoricalRates domain = HistoricalRates.builder()
                .startDate("2024-01-01")
                .endDate("2024-01-05")
                .rates(Map.of("2024-01-01", Map.of("USD", 0.00006)))
                .build();

        HistoricalRatesResponse mapped = HistoricalRatesResponse.builder()
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .rates(domain.getRates())
                .build();

        when(dataStore.get("historical_idr_usd")).thenReturn(domain);
        when(mapper.toHistoricalRatesResponse(domain)).thenReturn(mapped);

        ApiResponse<?> resp = controller.getFinanceData("historical_idr_usd");

        assertNotNull(resp);
        assertEquals(200, resp.getCode());
        assertSame(mapped, resp.getData());
        verify(mapper).toHistoricalRatesResponse(domain);
    }

    @Test
    void getFinanceData_returnsSupportedMapped() {
        SupportedCurrencies domain = SupportedCurrencies.builder()
                .currencies(Map.of("USD", "United States Dollar"))
                .build();

        SupportedCurrenciesResponse mapped = SupportedCurrenciesResponse.builder()
                .currencies(domain.getCurrencies())
                .build();

        when(dataStore.get("supported_currencies")).thenReturn(domain);
        when(mapper.toSupportedCurrenciesResponse(domain)).thenReturn(mapped);

        ApiResponse<?> resp = controller.getFinanceData("supported_currencies");

        assertNotNull(resp);
        assertEquals(200, resp.getCode());
        assertSame(mapped, resp.getData());
        verify(mapper).toSupportedCurrenciesResponse(domain);
    }

    @Test
    void getFinanceData_unknownKey_throwsBadRequest() {
        when(dataStore.get("not_exist")).thenReturn(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> controller.getFinanceData("not_exist"));
        assertTrue(ex.getMessage().contains("Unknown resource type"));
    }

    @Test
    void getFinanceData_unsupportedDomain_throwsBadRequest() {
        when(dataStore.get("weird")).thenReturn(new Object());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> controller.getFinanceData("weird"));
        assertTrue(ex.getMessage().contains("Unsupported domain object"));
    }
}
