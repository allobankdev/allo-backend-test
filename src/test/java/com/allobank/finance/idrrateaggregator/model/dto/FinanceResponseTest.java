package com.allobank.finance.idrrateaggregator.model.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceResponseTest {

    @Test
    void shouldCreateFinanceResponseWithKeyAndValue() {
        String key = "USD";
        Object value = "United States Dollar";

        FinanceResponse response = new FinanceResponse(key, value);

        assertThat(response.getKey()).isEqualTo(key);
        assertThat(response.getValue()).isEqualTo(value);
    }

    @Test
    void shouldSupportDifferentValueTypes() {
        FinanceResponse stringValue =
                new FinanceResponse("USD", "United States Dollar");

        FinanceResponse numericValue =
                new FinanceResponse("USD_RATE", 0.000064);

        assertThat(stringValue.getValue())
                .isInstanceOf(String.class);

        assertThat(numericValue.getValue())
                .isInstanceOf(Double.class);
    }
}
