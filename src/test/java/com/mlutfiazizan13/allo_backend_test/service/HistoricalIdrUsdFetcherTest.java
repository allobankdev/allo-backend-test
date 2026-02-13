package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.HistoricalRatesResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIdrUsdFetcher(restTemplate);
    }

    @Test
    void fetchData_shouldReturnHistoricalRates() {
        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        mockResponse.setAmount(BigDecimal.ONE);
        mockResponse.setBase("IDR");
        mockResponse.setStartDate("2024-01-01");
        mockResponse.setEndDate("2024-01-05");

        Map<String, Map<String, BigDecimal>> rates = new HashMap<>();
        Map<String, BigDecimal> dayRate = new HashMap<>();
        dayRate.put("USD", new BigDecimal("0.000064"));
        rates.put("2024-01-02", dayRate);
        mockResponse.setRates(rates);

        when(restTemplate.getForObject(
                "/2024-01-01..2024-01-05?from=IDR&to=USD",
                HistoricalRatesResponse.class))
                .thenReturn(mockResponse);

        Object result = fetcher.fetchData();

        assertThat(result).isInstanceOf(HistoricalRatesResponse.class);
        HistoricalRatesResponse response = (HistoricalRatesResponse) result;
        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getStartDate()).isEqualTo("2024-01-01");
        assertThat(response.getEndDate()).isEqualTo("2024-01-05");
        assertThat(response.getRates()).containsKey("2024-01-02");
        assertThat(response.getRates().get("2024-01-02").get("USD"))
                .isEqualByComparingTo(new BigDecimal("0.000064"));
    }

    @Test
    void fetchData_shouldThrowExternalApiExceptionOnNetworkFailure() {
        when(restTemplate.getForObject(
                "/2024-01-01..2024-01-05?from=IDR&to=USD",
                HistoricalRatesResponse.class))
                .thenThrow(new RestClientException("Connection timeout"));

        assertThatThrownBy(() -> fetcher.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch historical IDR/USD rates");
    }

    @Test
    void getStrategyType_shouldReturnCorrectType() {
        assertThat(fetcher.getStrategyType()).isEqualTo("historical_idr_usd");
    }
}
