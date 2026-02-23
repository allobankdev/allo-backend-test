package com.allobank.finance.idrrateaggregator.model.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LatestRatesResponseTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        LatestRatesResponse response = new LatestRatesResponse();

        double amount = 1.0;
        String base = "IDR";
        String date = "2026-02-03";
        Map<String, Double> rates = Map.of(
                "USD", 0.00006,
                "EUR", 0.000051
        );

        response.setAmount(amount);
        response.setBase(base);
        response.setDate(date);
        response.setRates(rates);

        assertThat(response.getAmount()).isEqualTo(amount);
        assertThat(response.getBase()).isEqualTo(base);
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getRates()).hasSize(2);
        assertThat(response.getRates().get("USD")).isEqualTo(0.00006);
    }

    @Test
    void ratesShouldSupportDifferentCurrencies() {
        LatestRatesResponse response = new LatestRatesResponse();

        Map<String, Double> rates = Map.of(
                "JPY", 0.00929,
                "KRW", 0.08638
        );

        response.setRates(rates);

        assertThat(response.getRates())
                .containsKeys("JPY", "KRW");
    }
}
