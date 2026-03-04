package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.dto.HistoricalRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new HistoricalRatesStrategy(webClient);
    }

    @Test
    void testGetResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.000064),
                        "2024-01-02", Map.of("USD", 0.000065),
                        "2024-01-03", Map.of("USD", 0.000063),
                        "2024-01-04", Map.of("USD", 0.000064),
                        "2024-01-05", Map.of("USD", 0.000066)
                )
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
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

                    // Check first item
                    HistoricalRate first = (HistoricalRate) list.get(0);
                    assertEquals(LocalDate.parse("2024-01-01"), first.getDate());
                    assertEquals(BigDecimal.valueOf(0.000064), first.getUsdRate());
                    assertEquals("historical_idr_usd", first.getResourceType());

                    // Check last item
                    HistoricalRate last = (HistoricalRate) list.get(list.size() - 1);
                    assertEquals(LocalDate.parse("2024-01-05"), last.getDate());
                    assertEquals(BigDecimal.valueOf(0.000066), last.getUsdRate());

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_EmptyRates() {
        // Arrange
        Map<String, Object> mockResponse = Map.of("rates", Map.of());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
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
    void testFetchData_NoRatesField() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(); // No rates field

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
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
    void testFetchData_WithNullUsdRate() {
        // Arrange - one entry has null USD rate
        Map<String, Object> mockResponse = Map.of(
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.000064),
                        "2024-01-02", Map.of("EUR", 0.000059), // No USD
                        "2024-01-03", Map.of("USD", 0.000063)
                )
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    List<?> list = (List<?>) response;
                    assertEquals(2, list.size()); // Only entries with USD
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testGetResponseType() {
        assertEquals(List.class, strategy.getResponseType());
    }
}