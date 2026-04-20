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

class LatestIdrRatesFetcherTest {

    private static final String GITHUB_USERNAME = "erlanggariansyah";
    private static final double EXPECTED_SPREAD_FACTOR = 0.00696;

    private static final String LATEST_RESPONSE_JSON = """
            {
              "amount": 1.0,
              "base": "IDR",
              "date": "2026-04-17",
              "rates": {
                "AUD": 0.000081,
                "USD": 0.000058,
                "EUR": 0.000049,
                "JPY": 0.00926
              }
            }
            """;

    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        WebClient mockWebClient = createMockWebClient(LATEST_RESPONSE_JSON, HttpStatus.OK);
        fetcher = new LatestIdrRatesFetcher(mockWebClient, new ObjectMapper(), GITHUB_USERNAME);
    }

    @Test
    @DisplayName("Should return correct resource type identifier")
    void getResourceType_shouldReturnLatestIdrRates() {
        assertThat(fetcher.getResourceType()).isEqualTo(ResourceConstants.LATEST_IDR_RATES);
    }

    @Test
    @DisplayName("Should correctly calculate spread factor from GitHub username")
    void calculateSpreadFactor_shouldComputeCorrectValue() {
        double factor = LatestIdrRatesFetcher.calculateSpreadFactor(GITHUB_USERNAME);
        assertThat(factor).isEqualTo(EXPECTED_SPREAD_FACTOR);
    }

    @Test
    @DisplayName("Should fetch data and include USD_BuySpread_IDR in response")
    @SuppressWarnings("unchecked")
    void fetchData_shouldReturnEnrichedDataWithSpread() {
        Map<String, Object> result = (Map<String, Object>) fetcher.fetchData();

        // Verify unified structure keys
        assertThat(result).containsKeys("amount", "base", "date", "rates");
        
        // Verify enrichment keys
        assertThat(result).containsKeys("githubUsername", "spreadFactor", "USD_BuySpread_IDR");
        
        assertThat(result.get("base")).isEqualTo(CurrencyConstants.IDR);
        assertThat(result.get("githubUsername")).isEqualTo(GITHUB_USERNAME);
        assertThat(result.get("spreadFactor")).isEqualTo(EXPECTED_SPREAD_FACTOR);
    }

    @Test
    @DisplayName("Should calculate USD_BuySpread_IDR correctly using the formula")
    @SuppressWarnings("unchecked")
    void fetchData_shouldCalculateUsdBuySpreadCorrectly() {
        Map<String, Object> result = (Map<String, Object>) fetcher.fetchData();

        double rateUsd = 0.000058;
        double expectedSpread = (1.0 / rateUsd) * (1.0 + EXPECTED_SPREAD_FACTOR);
        double actualSpread = (double) result.get("USD_BuySpread_IDR");

        assertThat(actualSpread).isCloseTo(expectedSpread, within(0.01));
    }

    @Test
    @DisplayName("Should throw ExternalApiException on server error")
    void fetchData_shouldThrowOnServerError() {
        WebClient errorClient = createMockWebClient("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        LatestIdrRatesFetcher errorFetcher = new LatestIdrRatesFetcher(errorClient, new ObjectMapper(), GITHUB_USERNAME);

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
