package com.allo.strategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.allo.dto.FinanceResourceResponse;
import com.allo.exception.ExternalApiException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LatestIDRRatesFetcher")
class LatestIDRRatesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private LatestIDRRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new LatestIDRRatesFetcher(restTemplate);
    }

    @Test
    @DisplayName("resourceType() returns 'latest_idr_rates'")
    void resourceType() {
        assertThat(fetcher.resourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    @DisplayName("Spread factor for 'yoelngl' equals 0.00762")
    void spreadFactorCalculation() {
        // y=121, o=111, e=101, l=108, n=110, g=103, l=108 → sum=762
        double expected = 762.0 / 100_000.0; // 0.00762
        assertThat(LatestIDRRatesFetcher.SPREAD_FACTOR).isCloseTo(expected, within(1e-10));
    }

    @Test
    @DisplayName("calculateSpreadFactor produces correct value for known input")
    void calculateSpreadFactorForKnownInput() {
        // "abc" → 97+98+99 = 294 → (294%1000)/100000 = 0.00294
        assertThat(LatestIDRRatesFetcher.calculateSpreadFactor("abc"))
                .isCloseTo(0.00294, within(1e-10));
    }

    @Test
    @DisplayName("fetch() returns enriched data with USD_BuySpread_IDR")
    @SuppressWarnings("unchecked")
    void fetchReturnsEnrichedData() {
        // Arrange: 1 USD = 0.000063 IDR (i.e. base=IDR, rate for USD)
        double usdRate = 0.000063;
        Map<String, Object> rates = Map.of("USD", usdRate, "EUR", 0.000059);
        Map<String, Object> apiBody = Map.of(
                "base", "IDR",
                "date", "2025-01-01",
                "rates", rates
        );
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(apiBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/latest?base=IDR"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<FinanceResourceResponse> result = fetcher.fetch();

        // Assert
        assertThat(result).hasSize(1);
        FinanceResourceResponse response = result.get(0);
        assertThat(response.resourceType()).isEqualTo("latest_idr_rates");

        Map<String, Object> data = (Map<String, Object>) response.data();
        assertThat(data).containsKey("USD_BuySpread_IDR");

        double expectedSpread = (1.0 / usdRate) * (1.0 + LatestIDRRatesFetcher.SPREAD_FACTOR);
        assertThat(((Number) data.get("USD_BuySpread_IDR")).doubleValue())
                .isCloseTo(expectedSpread, within(0.01));
        assertThat(((Number) data.get("spread_factor")).doubleValue())
                .isCloseTo(LatestIDRRatesFetcher.SPREAD_FACTOR, within(1e-10));
        assertThat(data.get("github_username")).isEqualTo("yoelngl");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException on network failure")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNetworkFailure() {
        when(restTemplate.exchange(
                eq("/latest?base=IDR"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Connection refused"));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Connection refused");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException when response is null")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNullBody() {
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>((Map<String, Object>) null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/latest?base=IDR"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("missing 'rates'");
    }
}
