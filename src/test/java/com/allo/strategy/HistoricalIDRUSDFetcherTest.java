package com.allo.strategy;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricalIDRUSDFetcher")
class HistoricalIDRUSDFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private HistoricalIDRUSDFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIDRUSDFetcher(restTemplate);
    }

    @Test
    @DisplayName("resourceType() returns 'historical_idr_usd'")
    void resourceType() {
        assertThat(fetcher.resourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    @DisplayName("fetch() returns historical data with default range")
    @SuppressWarnings("unchecked")
    void fetchReturnsHistoricalData() {
        Map<String, Object> apiBody = Map.of(
                "base", "IDR",
                "start_date", "2024-01-01",
                "end_date", "2024-01-05",
                "rates", Map.of(
                        "2024-01-02", Map.of("USD", 0.000064),
                        "2024-01-03", Map.of("USD", 0.000065)
                )
        );
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(apiBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/2024-01-01..2024-01-05?from=IDR&to=USD"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<FinanceResourceResponse> result = fetcher.fetch();

        assertThat(result).hasSize(1);
        FinanceResourceResponse response = result.get(0);
        assertThat(response.resourceType()).isEqualTo("historical_idr_usd");
        assertThat(response.data()).isNotNull();

        Map<String, Object> data = (Map<String, Object>) response.data();
        assertThat(data).containsKey("rates");
        assertThat(data).containsEntry("base", "IDR");
    }

    @Test
    @DisplayName("fetchByRange() returns historical data for custom range")
    @SuppressWarnings("unchecked")
    void fetchByRangeReturnsData() {
        Map<String, Object> apiBody = Map.of(
                "base", "IDR",
                "start_date", "2024-06-01",
                "end_date", "2024-06-10",
                "rates", Map.of(
                        "2024-06-03", Map.of("USD", 0.000063)
                )
        );
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(apiBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/2024-06-01..2024-06-10?from=IDR&to=USD"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<FinanceResourceResponse> result = fetcher.fetchByRange("2024-06-01", "2024-06-10");

        assertThat(result).hasSize(1);
        FinanceResourceResponse response = result.get(0);
        assertThat(response.resourceType()).isEqualTo("historical_idr_usd");

        Map<String, Object> data = (Map<String, Object>) response.data();
        assertThat(data).containsEntry("start_date", "2024-06-01");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException on network failure")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNetworkFailure() {
        when(restTemplate.exchange(
                eq("/2024-01-01..2024-01-05?from=IDR&to=USD"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Timeout"));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Timeout");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException on null response body")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNullBody() {
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>((Map<String, Object>) null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/2024-01-01..2024-01-05?from=IDR&to=USD"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Empty response");
    }
}
