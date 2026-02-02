package com.example.allotest.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatestRatesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestRatesFetcher latestRatesFetcher;

    private static final String TEST_GITHUB_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        latestRatesFetcher = new LatestRatesFetcher(webClient);
        // Set the github username using reflection
        ReflectionTestUtils.setField(latestRatesFetcher, "githubUsername", TEST_GITHUB_USERNAME);
    }

    @Test
    void fetchData_shouldReturnLatestRatesWithIDRBase_whenApiCallSucceeds() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("date", "2024-01-15");

        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", 0.000063);
        rates.put("EUR", 0.000058);
        rates.put("GBP", 0.000050);
        rates.put("JPY", 0.0092);
        mockResponse.put("rates", rates);

        // Mock WebClient behavior
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = latestRatesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result[0];
        assertNotNull(resultMap.get("rates"));
        assertTrue(resultMap.get("rates") instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Double> resultRates = (Map<String, Double>) resultMap.get("rates");
        assertEquals(4, resultRates.size());
        assertEquals(0.000063, resultRates.get("USD"));
        assertEquals(0.000058, resultRates.get("EUR"));

        // Verify correct endpoint was called
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/latest?base=IDR");
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(Object.class);
    }

    @Test
    void fetchData_shouldApplySpreadFactor_basedOnGithubUsername() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", 0.000063);
        mockResponse.put("rates", rates);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = latestRatesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result[0];

        // Verify that USD_BuySpread_IDR exists with spread applied
        assertNotNull(resultMap.get("USD_BuySpread_IDR"));
        assertTrue(resultMap.get("USD_BuySpread_IDR") instanceof Double);

        Double usdBuySpread = (Double) resultMap.get("USD_BuySpread_IDR");

        // Calculate expected spread factor for "testuser"
        double expectedSpreadFactor = calculateExpectedSpreadFactor(TEST_GITHUB_USERNAME);
        double expectedBuySpread = (1 / 0.000063) * (1 + expectedSpreadFactor);

        // Verify the spread was applied correctly
        assertEquals(expectedBuySpread, usdBuySpread, 0.01);
    }

    private double calculateExpectedSpreadFactor(String username) {
        int total = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            total += c;
        }
        return (total % 1000) / 100000.0;
    }

    @Test
    void fetchData_shouldUseCorrectEndpoint() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", 0.000063);
        mockResponse.put("rates", rates);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/latest?base=IDR")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        latestRatesFetcher.fetchData();

        // Assert - verify the exact endpoint
        verify(requestHeadersUriSpec, times(1)).uri("/latest?base=IDR");
    }

    @Test
    void fetchData_shouldHandleWebClientException() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.error(new RuntimeException("API connection failed")));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> latestRatesFetcher.fetchData());

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/latest?base=IDR");
    }

    @Test
    void fetchData_shouldHandleNullRates() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("rates", null);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = latestRatesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result[0];

        // Should not have rates or adjustedRates when rates is null
        assertNull(resultMap.get("rates"));
        assertNull(resultMap.get("adjustedRates"));
    }
}
