package com.allobank.test.service.strategy.impl;

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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@ExtendWith(MockitoExtension.class)
public class SupportedCurrenciesStrategyTest {

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
    private SupportedCurrenciesStrategy strategy;

    @Test
    void testFetchData() throws Exception {
        String json = "{\"IDR\": \"Indonesian Rupiah\", \"USD\": \"United States Dollar\"}";
        JsonNode jsonNode = objectMapper.readTree(json);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

        Object resultData = strategy.fetchData();
        assertNotNull(resultData);
        assertTrue(resultData instanceof java.util.List);
        java.util.List<Map<String, String>> result = (java.util.List<Map<String, String>>) resultData;

        assertTrue(result.stream()
                .anyMatch(m -> "IDR".equals(m.get("code")) && "Indonesian Rupiah".equals(m.get("name"))));
    }

    @Test
    void testGetCachedData() {
        Object expectedData = List.of(Map.of("test", "data"));
        when(dataCacheService.getData("supported_currencies")).thenReturn(expectedData);

        Object result = strategy.getCachedData();

        assertEquals(expectedData, result);
    }
}
