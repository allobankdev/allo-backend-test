package com.allobank.test.runner;

import com.allobank.test.service.DataCacheService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */

// test mendapatkan datanya
@SpringBootTest
public class StartupDataRunnerIntegrationTest {

    @Autowired
    private DataCacheService dataCacheService;

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public WebClient webClient() {
            WebClient webClient = Mockito.mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

            when(webClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("base", "IDR");
            node.put("amount", 1.0);
            node.put("date", "2024-01-01");
            ObjectNode rates = node.putObject("rates");
            rates.put("USD", 0.0001);

            // For supported currencies, it expects "USD": "Name"
            node.put("USD", "United States Dollar");
            node.put("IDR", "Indonesian Rupiah");

            when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(node));

            return webClient;
        }
    }

    @Test
    void testStartupDataLoaded() {
        Object latestData = dataCacheService.getData("latest_idr_rates");
        assertNotNull(latestData, "Latest rates should be loaded");
        Map<String, Object> latest = (Map<String, Object>) latestData;
        assertTrue(latest.containsKey("USD_BuySpread_IDR"));

        Object historicalData = dataCacheService.getData("historical_idr_usd");
        assertNotNull(historicalData, "Historical data should be loaded");
        assertTrue(historicalData instanceof java.util.List, "Historical data should be a List");

        Object currenciesData = dataCacheService.getData("supported_currencies");
        assertNotNull(currenciesData, "Supported currencies should be loaded");
        assertTrue(currenciesData instanceof java.util.List, "Supported currencies should be a List");
    }
}
