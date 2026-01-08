package com.allobank.test.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import com.allobank.test.service.DataCacheService;
import com.allobank.test.service.strategy.impl.HistoricalIDRUSDStrategy;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@ExtendWith(MockitoExtension.class)
public class HistoricalIDRUSDStrategyTest {

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

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private HistoricalIDRUSDStrategy strategy;

    @Test
    void testFetchData() throws Exception {
        String json = "{\"amount\": 1.0, \"base\": \"IDR\", \"rates\": {\"2024-01-01\": {\"USD\": 0.0001}}}";
        JsonNode jsonNode = objectMapper.readTree(json);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

        Object resultData = strategy.fetchData();
        assertNotNull(resultData);
        assertTrue(resultData instanceof java.util.List);
        java.util.List<Map<String, Object>> result = (java.util.List<Map<String, Object>>) resultData;

        assertEquals(1, result.size());
        assertEquals("2024-01-01", result.get(0).get("date"));
        assertEquals(0.0001, ((BigDecimal) result.get(0).get("rate")).doubleValue());
    }

    @Test
    void testGetCachedData() {
        Object expectedData = List.of(Map.of("test", "data"));
        when(dataCacheService.getData("historical_idr_usd")).thenReturn(expectedData);

        Object result = strategy.getCachedData();

        assertEquals(expectedData, result);
    }
}
