package com.allobank.test.service.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import com.allobank.test.service.DataCacheService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import java.util.List;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@ExtendWith(MockitoExtension.class)
public class LatestIDRRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private DataCacheService dataCacheService;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestIDRRatesStrategy strategy;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFetchData() {
        // Mock response
        String json = "{\"amount\": 1.0, \"base\": \"IDR\", \"date\": \"2024-01-01\", \"rates\": {\"USD\": 0.0001}}";
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

        Map<String, Object> result = (Map<String, Object>) strategy.fetchData();

        assertNotNull(result);
        assertEquals(0.0001, ((Map<String, BigDecimal>) result.get("rates")).get("USD").doubleValue());

        // Check calculation
        // User: antigravity-bot
        // Sum calculation:
        // a=97, n=110, t=116, i=105, g=103, r=114, a=97, v=118, i=105, t=116, y=121,
        // -=45, b=98, o=111, t=116
        // Sum = 1572
        // Spread Factor = (1572 % 1000) / 100000.0 = 572 / 100000.0 = 0.00572
        // Formula: (1 / 0.0001) * (1 + 0.00572) = 10000 * 1.00572 = 10057.2

        double expectedSpreadFactor = 0.00572;
        double expectedRate = (1 / 0.0001) * (1 + expectedSpreadFactor);

        assertEquals(expectedRate, (Double) result.get("USD_BuySpread_IDR"), 0.001);
    }

    @Test
    void testGetCachedData() {
        Object expectedData = Map.of("test", "data");
        when(dataCacheService.getData("latest_idr_rates")).thenReturn(expectedData);

        Object result = strategy.getCachedData();

        assertEquals(expectedData, result);
    }
}
