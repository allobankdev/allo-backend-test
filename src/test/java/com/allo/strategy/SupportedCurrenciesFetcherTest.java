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
@DisplayName("SupportedCurrenciesFetcher")
class SupportedCurrenciesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher(restTemplate);
    }

    @Test
    @DisplayName("resourceType() returns 'supported_currencies'")
    void resourceType() {
        assertThat(fetcher.resourceType()).isEqualTo("supported_currencies");
    }

    @Test
    @DisplayName("fetch() returns currency map from API")
    @SuppressWarnings("unchecked")
    void fetchReturnsCurrencyData() {
        Map<String, String> apiBody = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah"
        );
        ResponseEntity<Map<String, String>> responseEntity =
                new ResponseEntity<>(apiBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<FinanceResourceResponse> result = fetcher.fetch();

        assertThat(result).hasSize(1);
        FinanceResourceResponse response = result.get(0);
        assertThat(response.resourceType()).isEqualTo("supported_currencies");

        Map<String, String> data = (Map<String, String>) response.data();
        assertThat(data)
                .containsEntry("USD", "United States Dollar")
                .containsEntry("IDR", "Indonesian Rupiah");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException on network failure")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNetworkFailure() {
        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Service unavailable"));

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Service unavailable");
    }

    @Test
    @DisplayName("fetch() throws ExternalApiException on null response body")
    @SuppressWarnings("unchecked")
    void fetchThrowsOnNullBody() {
        ResponseEntity<Map<String, String>> responseEntity =
                new ResponseEntity<>((Map<String, String>) null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Empty response");
    }
}
