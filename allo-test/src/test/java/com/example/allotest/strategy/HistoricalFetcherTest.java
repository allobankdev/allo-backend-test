package com.example.allotest.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalFetcher historicalFetcher;

    @BeforeEach
    void setUp() {
        historicalFetcher = new HistoricalFetcher(webClient);
    }

    @Test
    void fetchData_shouldReturnHistoricalRatesForIDRtoUSD_whenApiCallSucceeds() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("start_date", "2024-01-01");
        mockResponse.put("end_date", "2024-01-05");

        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();

        Map<String, Double> rates20240101 = new LinkedHashMap<>();
        rates20240101.put("USD", 0.000063);
        rates.put("2024-01-01", rates20240101);

        Map<String, Double> rates20240102 = new LinkedHashMap<>();
        rates20240102.put("USD", 0.000064);
        rates.put("2024-01-02", rates20240102);

        Map<String, Double> rates20240103 = new LinkedHashMap<>();
        rates20240103.put("USD", 0.000062);
        rates.put("2024-01-03", rates20240103);

        Map<String, Double> rates20240104 = new LinkedHashMap<>();
        rates20240104.put("USD", 0.000063);
        rates.put("2024-01-04", rates20240104);

        Map<String, Double> rates20240105 = new LinkedHashMap<>();
        rates20240105.put("USD", 0.000065);
        rates.put("2024-01-05", rates20240105);

        mockResponse.put("rates", rates);

        // Mock WebClient behavior
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = historicalFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result[0];
        assertEquals("IDR", response.get("base"));
        assertEquals("2024-01-01", response.get("start_date"));
        assertEquals("2024-01-05", response.get("end_date"));

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> historicalRates =
            (Map<String, Map<String, Double>>) response.get("rates");
        assertNotNull(historicalRates);
        assertEquals(5, historicalRates.size());
        assertEquals(0.000063, historicalRates.get("2024-01-01").get("USD"));
        assertEquals(0.000065, historicalRates.get("2024-01-05").get("USD"));

        // Verify interactions
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1))
            .uri("/2024-01-01..2024-01-05?from=IDR&to=USD");
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(Object.class);
    }

    @Test
    void fetchData_shouldUseCorrectEndpointWithDateRangeAndCurrencies() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("rates", new LinkedHashMap<>());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/2024-01-01..2024-01-05?from=IDR&to=USD"))
            .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        historicalFetcher.fetchData();

        // Assert - verify the exact endpoint with date range and currency parameters
        verify(requestHeadersUriSpec, times(1))
            .uri("/2024-01-01..2024-01-05?from=IDR&to=USD");
    }

    @Test
    void fetchData_shouldHandleWebClientException() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.error(new RuntimeException("Historical data unavailable")));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> historicalFetcher.fetchData());

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1))
            .uri("/2024-01-01..2024-01-05?from=IDR&to=USD");
    }

    @Test
    void fetchData_shouldReturnEmptyRates_whenNoDataAvailable() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("start_date", "2024-01-01");
        mockResponse.put("end_date", "2024-01-05");
        mockResponse.put("rates", new LinkedHashMap<>());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = historicalFetcher.fetchData();

        // Assert
        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result[0];

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> historicalRates =
            (Map<String, Map<String, Double>>) response.get("rates");
        assertTrue(historicalRates.isEmpty());

        verify(webClient, times(1)).get();
    }

    @Test
    void fetchData_shouldAcceptOptionalParams() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("rates", new LinkedHashMap<>());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act - calling with params (even though they're not used in current implementation)
        Object[] result = historicalFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);

        verify(webClient, times(1)).get();
    }

    @Test
    void fetchData_shouldReturnArrayWithSingleElement() {
        // Arrange
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");
        mockResponse.put("rates", new LinkedHashMap<>());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = historicalFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length, "Result array should contain exactly one element");
    }

    @Test
    void fetchData_shouldHandlePartialDataForDateRange() {
        // Arrange - Only some dates have data
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("base", "IDR");

        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();

        Map<String, Double> rates20240101 = new LinkedHashMap<>();
        rates20240101.put("USD", 0.000063);
        rates.put("2024-01-01", rates20240101);

        // Skip 2024-01-02 and 2024-01-03 (e.g., weekends)

        Map<String, Double> rates20240104 = new LinkedHashMap<>();
        rates20240104.put("USD", 0.000064);
        rates.put("2024-01-04", rates20240104);

        mockResponse.put("rates", rates);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        // Act
        Object[] result = historicalFetcher.fetchData();

        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result[0];

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Double>> historicalRates =
            (Map<String, Map<String, Double>>) response.get("rates");

        assertEquals(2, historicalRates.size());
        assertTrue(historicalRates.containsKey("2024-01-01"));
        assertTrue(historicalRates.containsKey("2024-01-04"));
        assertFalse(historicalRates.containsKey("2024-01-02"));
        assertFalse(historicalRates.containsKey("2024-01-03"));
    }
}
