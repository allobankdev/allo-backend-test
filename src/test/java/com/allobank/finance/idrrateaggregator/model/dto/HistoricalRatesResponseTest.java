package com.allobank.finance.idrrateaggregator.model.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoricalRatesResponseTest {

    @Test
    void shouldSetAndGetRatesCorrectly() {
        Map<String, Map<String, Double>> rates = Map.of(
                "2023-12-29", Map.of("USD", 0.000065),
                "2024-01-02", Map.of("USD", 0.000064)
        );

        HistoricalRatesResponse response = new HistoricalRatesResponse();

        response.setRates(rates);

        assertThat(response.getRates()).isNotNull();
        assertThat(response.getRates()).hasSize(2);
        assertThat(response.getRates().get("2023-12-29").get("USD"))
                .isEqualTo(0.000065);
    }

    @Test
    void ratesShouldSupportMultipleCurrenciesPerDate() {
        Map<String, Map<String, Double>> rates = Map.of(
                "2024-01-05", Map.of(
                        "USD", 0.000064,
                        "EUR", 0.000059
                )
        );

        HistoricalRatesResponse response = new HistoricalRatesResponse();

        response.setRates(rates);

        assertThat(response.getRates().get("2024-01-05"))
                .containsKeys("USD", "EUR");
    }
}
