package com.allobank.backendtest.strategy;

import com.allobank.backendtest.constant.CurrencyConstants;
import com.allobank.backendtest.constant.ResourceConstants;
import com.allobank.backendtest.exception.ExternalApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HistoricalIdrUsdFetcherTest {

    private static final String HISTORICAL_RESPONSE_JSON = """
            {
              "amount": 1.0,
              "base": "IDR",
              "start_date": "2023-12-29",
              "end_date": "2024-01-05",
              "rates": {
                "2023-12-29": { "USD": 0.000065 },
                "2024-01-02": { "USD": 0.000064 },
                "2024-01-03": { "USD": 0.000064 },
                "2024-01-04": { "USD": 0.000064 },
                "2024-01-05": { "USD": 0.000064 }
              }
            }
            """;

    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        WebClient mockWebClient = createMockWebClient(HISTORICAL_RESPONSE_JSON, HttpStatus.OK);
        fetcher = new HistoricalIdrUsdFetcher(mockWebClient, new ObjectMapper(), "2024-01-01", "2024-01-05");
    }

    @Test
    @DisplayName("Should return correct resource type identifier")
    void getResourceType_shouldReturnHistoricalIdrUsd() {
        assertThat(fetcher.getResourceType()).isEqualTo(ResourceConstants.HISTORICAL_IDR_USD);
    }

    @Test
    @DisplayName("Should fetch and deserialize historical data correctly")
    @SuppressWarnings("unchecked")
    void fetchData_shouldReturnCorrectStructure() {
        Map<String, Object> result = (Map<String, Object>) fetcher.fetchData();

        // Verify top-level structure
        assertThat(result).containsKeys("amount", "base", "startDate", "endDate", "rates");
        
        assertThat(result.get("amount")).isEqualTo(1.0);
        assertThat(result.get("base")).isEqualTo(CurrencyConstants.IDR);
        assertThat(result.get("startDate")).isEqualTo("2023-12-29");
        assertThat(result.get("endDate")).isEqualTo("2024-01-05");
    }

    @Test
    @DisplayName("Should contain all date entries in rates")
    @SuppressWarnings("unchecked")
    void fetchData_shouldContainAllDateEntries() {
        Map<String, Object> result = (Map<String, Object>) fetcher.fetchData();
        Map<String, Map<String, Double>> rates = (Map<String, Map<String, Double>>) result.get("rates");

        assertThat(rates).hasSize(5);
        assertThat(rates).containsKeys("2023-12-29", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05");
    }

    @Test
    @DisplayName("Should throw ExternalApiException on server error")
    void fetchData_shouldThrowOnServerError() {
        WebClient errorClient = createMockWebClient("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        HistoricalIdrUsdFetcher errorFetcher = new HistoricalIdrUsdFetcher(
                errorClient, new ObjectMapper(), "2024-01-01", "2024-01-05");

        assertThatThrownBy(errorFetcher::fetchData)
                .isInstanceOf(ExternalApiException.class);
    }

    private WebClient createMockWebClient(String responseBody, HttpStatus status) {
        ExchangeFunction exchangeFunction = clientRequest ->
                Mono.just(ClientResponse.create(status)
                        .header("Content-Type", "application/json")
                        .body(responseBody)
                        .build());

        return WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
    }
}
