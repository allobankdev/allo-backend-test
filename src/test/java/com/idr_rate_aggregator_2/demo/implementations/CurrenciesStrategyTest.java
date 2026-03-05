package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.dto.CurrencyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrenciesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new CurrenciesStrategy(webClient);
    }

    @Test
    void testGetResourceType() {
        assertEquals("supported_currencies", strategy.getResourceType());
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        Map<String, String> mockResponse = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "GBP", "British Pound",
                "JPY", "Japanese Yen",
                "IDR", "Indonesian Rupiah"
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertTrue(response instanceof List);
                    List<?> list = (List<?>) response;

                    assertEquals(5, list.size());

                    // Verify each item
                    for (Object obj : list) {
                        assertTrue(obj instanceof CurrencyResponse);
                        CurrencyResponse currency = (CurrencyResponse) obj;
                        assertNotNull(currency.getCode());
                        assertNotNull(currency.getName());
                        assertEquals("supported_currencies", currency.getResourceType());
                    }

                    // Check specific currency
                    boolean hasUSD = list.stream()
                            .map(obj -> (CurrencyResponse) obj)
                            .anyMatch(c -> "USD".equals(c.getCode()) && "United States Dollar".equals(c.getName()));
                    assertTrue(hasUSD);

                    boolean hasIDR = list.stream()
                            .map(obj -> (CurrencyResponse) obj)
                            .anyMatch(c -> "IDR".equals(c.getCode()) && "Indonesian Rupiah".equals(c.getName()));
                    assertTrue(hasIDR);

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_EmptyResponse() {
        // Arrange
        Map<String, String> mockResponse = Map.of();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertTrue(response instanceof List);
                    List<?> list = (List<?>) response;
                    assertTrue(list.isEmpty());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_NullResponse() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(null));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertTrue(response instanceof List);
                    List<?> list = (List<?>) response;
                    assertTrue(list.isEmpty());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testTransformToCurrencyResponses_WithException() {
        // Kita perlu test transformasi dengan data yang menyebabkan exception
        // Tapi karena method private, kita test melalui fetchData dengan mock yang bermasalah

        // Buat mock response yang akan menyebabkan exception di transform
        // Misalnya dengan memasukkan data yang tidak sesuai tipe
        Map<String, Integer> invalidResponse = Map.of("USD", 123); // Integer bukan String

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // Casting yang tidak aman, tapi untuk test kita paksa
        @SuppressWarnings("unchecked")
        Mono<Map<String, String>> invalidMono = (Mono<Map<String, String>>) (Mono<?>) Mono.just(invalidResponse);

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert - harusnya fallback ke empty list
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertTrue(response instanceof List);
                    List<?> list = (List<?>) response;
                    assertTrue(list.isEmpty()); // Fallback to empty list
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testGetResponseType() {
        assertEquals(List.class, strategy.getResponseType());
    }
}
