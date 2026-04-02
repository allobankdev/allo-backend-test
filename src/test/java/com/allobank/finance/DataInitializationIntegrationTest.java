package com.allobank.finance;

import com.allobank.finance.repository.FinanceDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DataInitializationIntegrationTest {

    @Autowired
    private FinanceDataRepository financeDataRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public WebClient webClient() {
            return WebClient.builder()
                    .exchangeFunction(clientRequest -> {
                        String url = clientRequest.url().toString();
                        String body = "{}";
                        if (url.contains("/latest")) {
                            body = "{\"base\":\"IDR\",\"date\":\"2024-03-08\",\"rates\":{\"USD\":0.000065}}";
                        } else if (url.contains("from=IDR&to=USD")) {
                            body = "{\"base\":\"IDR\",\"start_date\":\"2024-01-01\",\"end_date\":\"2024-01-05\",\"rates\":{\"2024-01-01\":{\"USD\":0.000065}}}";
                        } else if (url.contains("/currencies")) {
                            body = "{\"USD\":\"United States Dollar\",\"IDR\":\"Indonesian Rupiah\"}";
                        }
                        
                        return Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body(body)
                                .build());
                    })
                    .build();
        }
    }

    @Test
    void testDataInitialization() {
        List<Map<String, Object>> latest = financeDataRepository.findDataByResourceType("latest_idr_rates").orElseThrow();
        List<Map<String, Object>> historical = financeDataRepository.findDataByResourceType("historical_idr_usd").orElseThrow();
        List<Map<String, Object>> currencies = financeDataRepository.findDataByResourceType("supported_currencies").orElseThrow();

        assertEquals(1, latest.size());
        assertEquals(1, historical.size());
        assertFalse(currencies.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> latest.add(Map.of()));
        @SuppressWarnings("unchecked")
        Map<String, Object> latestPayload = (Map<String, Object>) latest.get(0).get("data");
        assertThrows(UnsupportedOperationException.class, () -> ((Map<String, Object>) latestPayload.get("rates")).put("EUR", 1.0));
    }
}
