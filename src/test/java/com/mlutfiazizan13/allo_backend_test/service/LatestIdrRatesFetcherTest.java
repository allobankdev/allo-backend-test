package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.LatestRatesResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new LatestIdrRatesFetcher(restTemplate);
    }

    @Test
    void fetchData_shouldReturnLatestRatesWithSpreadCalculation() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setAmount(BigDecimal.ONE);
        mockResponse.setBase("IDR");
        mockResponse.setDate("2025-02-11");
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.0000636998"));
        rates.put("EUR", new BigDecimal("0.0000612345"));
        mockResponse.setRates(rates);

        when(restTemplate.getForObject("/latest?base=IDR", LatestRatesResponse.class))
                .thenReturn(mockResponse);

        Object result = fetcher.fetchData();

        assertThat(result).isInstanceOf(LatestRatesResponse.class);
        LatestRatesResponse response = (LatestRatesResponse) result;
        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates()).containsKey("USD");
        assertThat(response.getUsdBuySpreadIdr()).isNotNull();

        // Verify spread calculation: (1 / Rate_USD) * (1 + SpreadFactor)
        BigDecimal spreadFactor = LatestIdrRatesFetcher.calculateSpreadFactor("mlutfiazizan13");
        BigDecimal expectedInverse = BigDecimal.ONE.divide(
                new BigDecimal("0.0000636998"), MathContext.DECIMAL128);
        BigDecimal expected = expectedInverse.multiply(
                BigDecimal.ONE.add(spreadFactor), MathContext.DECIMAL128);
        assertThat(response.getUsdBuySpreadIdr()).isEqualByComparingTo(expected);
    }

    @Test
    void fetchData_shouldHandleMissingUsdRate() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setAmount(BigDecimal.ONE);
        mockResponse.setBase("IDR");
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.0000612345"));
        mockResponse.setRates(rates);

        when(restTemplate.getForObject("/latest?base=IDR", LatestRatesResponse.class))
                .thenReturn(mockResponse);

        Object result = fetcher.fetchData();

        LatestRatesResponse response = (LatestRatesResponse) result;
        assertThat(response.getUsdBuySpreadIdr()).isNull();
    }

    @Test
    void fetchData_shouldThrowExternalApiExceptionOnNetworkFailure() {
        when(restTemplate.getForObject("/latest?base=IDR", LatestRatesResponse.class))
                .thenThrow(new RestClientException("Connection refused"));

        assertThatThrownBy(() -> fetcher.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch latest IDR rates");
    }

    @Test
    void calculateSpreadFactor_shouldReturnCorrectValue() {
        // mlutfiazizan13 -> sum=1410, 1410%1000=410, 410/100000.0=0.00410
        BigDecimal factor = LatestIdrRatesFetcher.calculateSpreadFactor("mlutfiazizan13");
        assertThat(factor).isEqualByComparingTo(new BigDecimal("0.0041"));
    }

    @Test
    void getStrategyType_shouldReturnCorrectType() {
        assertThat(fetcher.getStrategyType()).isEqualTo("latest_idr_rates");
    }
}
