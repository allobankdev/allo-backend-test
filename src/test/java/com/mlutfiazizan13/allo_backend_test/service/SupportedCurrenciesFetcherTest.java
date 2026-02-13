package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.CurrencyMapResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher(restTemplate);
    }

    @Test
    void fetchData_shouldReturnWrappedCurrencyMap() {
        Map<String, String> mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("AUD", "Australian Dollar");
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("IDR", "Indonesian Rupiah");

        ResponseEntity<Map<String, String>> responseEntity =
                new ResponseEntity<>(mockCurrencies, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        Object result = fetcher.fetchData();

        assertThat(result).isInstanceOf(CurrencyMapResponse.class);
        CurrencyMapResponse response = (CurrencyMapResponse) result;
        assertThat(response.getCurrencies()).hasSize(3);
        assertThat(response.getCurrencies()).containsEntry("IDR", "Indonesian Rupiah");
        assertThat(response.getCurrencies()).containsEntry("USD", "United States Dollar");
    }

    @Test
    void fetchData_shouldThrowExternalApiExceptionOnNetworkFailure() {
        when(restTemplate.exchange(
                eq("/currencies"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RestClientException("Service unavailable"));

        assertThatThrownBy(() -> fetcher.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch supported currencies");
    }

    @Test
    void getStrategyType_shouldReturnCorrectType() {
        assertThat(fetcher.getStrategyType()).isEqualTo("supported_currencies");
    }
}
