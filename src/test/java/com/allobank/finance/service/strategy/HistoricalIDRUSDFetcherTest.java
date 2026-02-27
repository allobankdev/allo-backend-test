package com.allobank.finance.service.strategy;

import com.allobank.finance.config.properties.FrankfurterProperties;
import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.integration.frankfurter.dto.HistoricalRatesRawResponseBuilder;
import com.allobank.finance.model.response.HistoricalRatesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class HistoricalIDRUSDFetcherTest {

    @InjectMocks
    HistoricalIDRUSDFetcher fetcher;

    @Mock
    FrankfurterClient client;

    @Mock
    FrankfurterProperties properties;

    @Mock
    FrankfurterProperties.Resources resources;

    @Mock
    FrankfurterProperties.Resources.Historical historical;

    @Test
    void shouldMapHistoricalRatesCorrectly() {
        var targetCurrency = "USD";

        Mockito.when(properties.resources()).thenReturn(resources);
        Mockito.when(resources.historical()).thenReturn(historical);
        Mockito.when(historical.to()).thenReturn(targetCurrency);

        var rates = Map.of(
                "2024-01-01", Map.of("USD", 0.000064),
                "2024-01-02", Map.of("USD", 0.000065)
        );

        var raw = HistoricalRatesRawResponseBuilder.builder()
                .amount(0)
                .base(targetCurrency)
                .rates(rates)
                .build();

        Mockito.when(client.fetchHistoricalRates()).thenReturn(raw);

        var result = (List<HistoricalRatesResponse>) fetcher.fetchData();

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("2024-01-02", result.get(0).date());
        Assertions.assertEquals(0.000065, result.get(0).rate());

        Assertions.assertEquals("2024-01-01", result.get(1).date());
        Assertions.assertEquals(0.000064, result.get(1).rate());

    }
}
