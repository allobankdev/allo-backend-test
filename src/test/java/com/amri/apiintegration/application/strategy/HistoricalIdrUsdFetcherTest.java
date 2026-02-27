package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.HistoricalRatesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private CurrencyRatesGateway currencyRatesGateway;

    @Test
    void fetch_shouldReturnHistoricalPayloadForConfiguredDateRange() {
        HistoricalRatesDto historicalRatesDto = new HistoricalRatesDto(
                BigDecimal.ONE,
                "IDR",
                Map.of("2024-01-01", Map.of("USD", new BigDecimal("0.000064")))
        );
        when(currencyRatesGateway.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD"))
                .thenReturn(historicalRatesDto);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(currencyRatesGateway);

        var result = fetcher.fetch();

        assertEquals("historical_idr_usd", result.resourceType());
        assertEquals(historicalRatesDto, result.data());
        verify(currencyRatesGateway).getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD");
    }
}
