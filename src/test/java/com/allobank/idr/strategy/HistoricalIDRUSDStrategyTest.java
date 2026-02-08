package com.allobank.idr.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIDRUSDStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIDRUSDStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new HistoricalIDRUSDStrategy(webClient);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
    }

    @Test
    void shouldFetchHistoricalData() {
        Map<String, Object> mockResponse = Map.of(
            "amount", 1.0,
            "base", "IDR",
            "start_date", "2024-01-01",
            "end_date", "2024-01-05",
            "rates", Map.of(
                "2024-01-01", Map.of("USD", 0.000063),
                "2024-01-02", Map.of("USD", 0.000064)
            )
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        Map<String, Object> result = strategy.fetchData();

        assertNotNull(result);
        assertEquals("IDR", result.get("base"));
        assertTrue(result.containsKey("rates"));
        
        verify(webClient).get();
    }
}
