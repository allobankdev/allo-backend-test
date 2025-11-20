package com.allobank.assignment.support;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.model.HistoricalRatesResponse;
import com.allobank.assignment.model.LatestRatesResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class FrankfurterClientStubConfig {

    @Bean
    @Primary
    FrankfurterApiClient frankfurterApiClientStub() {
        FrankfurterApiClient client = mock(FrankfurterApiClient.class);
        LatestRatesResponse latest = new LatestRatesResponse(
                BigDecimal.ONE,
                "IDR",
                LocalDate.of(2024, 1, 5),
                Map.of("USD", new BigDecimal("0.000064")));
        when(client.getLatestRates("IDR")).thenReturn(latest);

        HistoricalRatesResponse historical = new HistoricalRatesResponse(
                BigDecimal.ONE,
                "IDR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5),
                Map.of("2024-01-02", Map.of("USD", new BigDecimal("0.000064"))));
        when(client.getHistoricalRates("IDR", "USD", "2024-01-01", "2024-01-05"))
                .thenReturn(historical);

        when(client.getSupportedCurrencies())
                .thenReturn(Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah"));

        return client;
    }

}
