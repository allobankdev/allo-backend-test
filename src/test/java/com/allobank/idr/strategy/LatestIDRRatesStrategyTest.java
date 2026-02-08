package com.allobank.idr.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestIDRRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LatestIDRRatesStrategy(webClient);
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }

    @Test
    void shouldFetchAndCalculateSpread() {
        Map<String, Object> mockResponse = Map.of(
            "base", "IDR",
            "rates", Map.of("USD", 0.000063)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        Map<String, Object> result = strategy.fetchData();

        assertNotNull(result);
        assertTrue(result.containsKey("USD_BuySpread_IDR"));
        assertTrue(result.containsKey("spread_factor"));
        
        double spreadFactor = (double) result.get("spread_factor");
        assertTrue(spreadFactor >= 0.0 && spreadFactor < 0.01);
        
        verify(webClient).get();
    }

    @Test
    void shouldCalculateSpreadFactorCorrectly() {
        String username = "testuser";
        int expectedSum = username.toLowerCase().chars().sum();
        double expectedSpreadFactor = (expectedSum % 1000) / 100000.0;

        ReflectionTestUtils.setField(strategy, "githubUsername", username);

        Map<String, Object> mockResponse = Map.of(
            "base", "IDR",
            "rates", Map.of("USD", 0.000063)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        Map<String, Object> result = strategy.fetchData();
        
        assertEquals(expectedSpreadFactor, result.get("spread_factor"));
    }
}
