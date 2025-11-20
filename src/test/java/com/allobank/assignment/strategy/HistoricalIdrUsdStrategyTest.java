package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.config.FrankfurterApiProperties;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.HistoricalRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class HistoricalIdrUsdStrategyTest {
    private FrankfurterApiClient apiClient;
    private FrankfurterApiProperties properties;
    private HistoricalIdrUsdStrategy strategy;

    @BeforeEach
    void setUp() {
        apiClient = mock(FrankfurterApiClient.class);
        properties = new FrankfurterApiProperties();
        properties.getHistorical().setStartDate(LocalDate.of(2024, 1, 1));
        properties.getHistorical().setEndDate(LocalDate.of(2024, 1, 5));
        properties.getHistorical().setFrom("IDR");
        properties.getHistorical().setTo("USD");
        strategy = new HistoricalIdrUsdStrategy(apiClient, properties);
    }

    @Test
    void shouldWrapHistoricalResponse() {
        HistoricalRatesResponse response = new HistoricalRatesResponse(
                BigDecimal.ONE,
                "IDR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5),
                Map.of("2024-01-02", Map.of("USD", new BigDecimal("0.000064"))));
        given(apiClient.getHistoricalRates("IDR", "USD", "2024-01-01", "2024-01-05")).willReturn(response);

        FinanceDataResponse result = strategy.fetch();

        assertThat(result.resourceType()).isEqualTo("historical_idr_usd");
        assertThat(result.payload()).isEqualTo(response);
    }
}
