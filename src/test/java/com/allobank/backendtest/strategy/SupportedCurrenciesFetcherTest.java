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

class SupportedCurrenciesFetcherTest {

    private static final String CURRENCIES_RESPONSE_JSON = """
            {
              "AUD": "Australian Dollar",
              "BRL": "Brazilian Real",
              "EUR": "Euro",
              "IDR": "Indonesian Rupiah",
              "USD": "United States Dollar"
            }
            """;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        WebClient mockWebClient = createMockWebClient(CURRENCIES_RESPONSE_JSON, HttpStatus.OK);
        fetcher = new SupportedCurrenciesFetcher(mockWebClient, new ObjectMapper());
    }

    @Test
    @DisplayName("Should return correct resource type identifier")
    void getResourceType_shouldReturnSupportedCurrencies() {
        assertThat(fetcher.getResourceType()).isEqualTo(ResourceConstants.SUPPORTED_CURRENCIES);
    }

    @Test
    @DisplayName("Should fetch and deserialize currencies correctly")
    @SuppressWarnings("unchecked")
    void fetchData_shouldReturnCurrencyMap() {
        Map<String, String> result = (Map<String, String>) fetcher.fetchData();

        assertThat(result).hasSize(5);
        assertThat(result).containsEntry(CurrencyConstants.IDR, "Indonesian Rupiah");
        assertThat(result).containsEntry(CurrencyConstants.USD, "United States Dollar");
    }

    @Test
    @DisplayName("Should throw ExternalApiException on server error")
    void fetchData_shouldThrowOnServerError() {
        WebClient errorClient = createMockWebClient("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        SupportedCurrenciesFetcher errorFetcher = new SupportedCurrenciesFetcher(errorClient, new ObjectMapper());

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
