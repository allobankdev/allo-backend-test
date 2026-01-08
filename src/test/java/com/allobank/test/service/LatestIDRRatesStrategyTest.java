package com.allobank.test.service;

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
import com.allobank.test.service.strategy.impl.LatestIDRRatesStrategy;

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
        // User: codelamps-academy
        // Sum calculation:
        // c=99, o=111, d=100, e=101, l=108, a=97, m=109, p=112, s=115,
        // -=45, a=97, c=99, a=97, d=100, e=101, m=109, y=121
        // Sum = 1721
        // Spread Factor = (1721 % 1000) / 100000.0 = 721 / 100000.0 = 0.00721
        // Formula: (1 / 0.0001) * (1 + 0.00721) = 10000 * 1.00721 = 10072.1

        double expectedSpreadFactor = 0.00721;
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
