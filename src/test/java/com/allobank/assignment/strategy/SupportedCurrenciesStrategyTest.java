package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.model.FinanceDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SupportedCurrenciesStrategyTest {
    private FrankfurterApiClient apiClient;
    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        apiClient = mock(FrankfurterApiClient.class);
        strategy = new SupportedCurrenciesStrategy(apiClient);
    }

    @Test
    void shouldReturnSupportedCurrenciesPayload() {
        given(apiClient.getSupportedCurrencies()).willReturn(Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah"));

        FinanceDataResponse result = strategy.fetch();

        assertThat(result.resourceType()).isEqualTo("supported_currencies");
        assertThat(result.payload()).isInstanceOf(Map.class);
        Map<?, ?> payload = (Map<?, ?>) result.payload();
        assertThat(payload.get("USD")).isEqualTo("United States Dollar");
    }
}
