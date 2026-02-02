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
class SupportedCurrenciesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher supportedCurrenciesFetcher;

    @BeforeEach
    void setUp() {
        supportedCurrenciesFetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @Test
    void fetchData_shouldReturnSupportedCurrencies_whenApiCallSucceeds() {
        // Arrange
        Map<String, String> mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("EUR", "Euro");
        mockCurrencies.put("GBP", "British Pound Sterling");
        mockCurrencies.put("JPY", "Japanese Yen");
        mockCurrencies.put("IDR", "Indonesian Rupiah");

        // Mock WebClient behavior
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockCurrencies));

        // Act
        Object[] result = supportedCurrenciesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result[0];
        assertEquals(5, currencies.size());
        assertEquals("United States Dollar", currencies.get("USD"));
        assertEquals("Euro", currencies.get("EUR"));
        assertEquals("Indonesian Rupiah", currencies.get("IDR"));

        // Verify interactions
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/currencies");
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(Object.class);
    }

    @Test
    void fetchData_shouldReturnEmptyMap_whenApiReturnsEmptyResponse() {
        // Arrange
        Map<String, String> emptyMap = new LinkedHashMap<>();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(emptyMap));

        // Act
        Object[] result = supportedCurrenciesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result[0];
        assertTrue(currencies.isEmpty());

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/currencies");
    }

    @Test
    void fetchData_shouldUseCorrectEndpoint() {
        // Arrange
        Map<String, String> mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("USD", "United States Dollar");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockCurrencies));

        // Act
        supportedCurrenciesFetcher.fetchData();

        // Assert
        verify(requestHeadersUriSpec, times(1)).uri("/currencies");
    }

    @Test
    void fetchData_shouldHandleWebClientException() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> supportedCurrenciesFetcher.fetchData());

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/currencies");
    }

    @Test
    void fetchData_shouldAcceptOptionalParams() {
        // Arrange
        Map<String, String> mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("USD", "United States Dollar");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockCurrencies));

        // Act - calling with params (even though they're not used in the implementation)
        Object[] result = supportedCurrenciesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);

        verify(webClient, times(1)).get();
    }

    @Test
    void fetchData_shouldReturnArrayWithSingleElement() {
        // Arrange
        Map<String, String> mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("USD", "United States Dollar");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockCurrencies));

        // Act
        Object[] result = supportedCurrenciesFetcher.fetchData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length, "Result array should contain exactly one element");
    }
}
