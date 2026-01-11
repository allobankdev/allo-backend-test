package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.EnhancedLatestData;
import com.prasetyahs.allo.finance.model.LatestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIDRRatesFetcherTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestIDRRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        // "prasetyahs" -> sum=1092 -> spreadFactor=0.00092
        fetcher = new LatestIDRRatesFetcher("prasetyahs");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchAndProcess_CalculatesSpreadCorrectly() {
        // Mock Response: 1 USD = 15000 IDR (Wait, base=IDR? So 1 IDR = 0.000066 USD)
        // Usually API: /latest?base=IDR -> rates: {"USD": 0.000064...}
        // Let's assume rate is 0.0001 for easy math.

        LatestResponse mockResponse = new LatestResponse(
                1.0, "IDR", "2024-01-01", Map.of("USD", 0.0001));

        when(responseSpec.bodyToMono(LatestResponse.class)).thenReturn(Mono.just(mockResponse));

        Object result = fetcher.fetchAndProcess(webClient);

        assertNotNull(result);
        EnhancedLatestData data = (EnhancedLatestData) result;

        // Spread Factor for prasetyahs = 0.00092
        // USD_BuySpread_IDR = (1 / 0.0001) * (1 + 0.00092)
        // = 10000 * 1.00092 = 10009.2

        assertEquals(10009.2, data.usdBuySpreadIdr(), 0.001);
        assertEquals("IDR", data.base());
    }
}
