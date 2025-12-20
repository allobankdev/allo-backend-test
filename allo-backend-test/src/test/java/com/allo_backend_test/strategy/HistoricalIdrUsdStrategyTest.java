package com.allo_backend_test.strategy;

import com.allo_backend_test.client.FrankfurterApiClient;
import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.dto.HistoricalRatesResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HistoricalIdrUsdStrategyTest {

    @Mock
    private FrankfurterApiClient apiClient;

    @InjectMocks
    private HistoricalIdrUsdStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchHistoricalIdrUsdRates() {

        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        mockResponse.setRates(
                Map.of(
                        "2024-01-01", Map.of("USD", 0.000065),
                        "2024-01-02", Map.of("USD", 0.000066)
                )
        );

        when(apiClient.fetchHistoricalIdrUsd())
                .thenReturn(mockResponse);

        // when
        List<FinanceResponseDto> result = strategy.fetch();

        // then
        assertThat(result).hasSize(2);

        FinanceResponseDto first = result.get(0);
        FinanceResponseDto second = result.get(1);

        assertThat(first.getKey()).isEqualTo("2024-01-01");
        assertThat((Double) first.getValue()).isEqualTo(0.000065);

        assertThat(second.getKey()).isEqualTo("2024-01-02");
        assertThat((Double) second.getValue()).isEqualTo(0.000066);
    }

}
