package com.example.finance.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class LatestIdrRatesFetcherTest {

    private RestTemplate restTemplate;
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new LatestIdrRatesFetcher(restTemplate, "testuser");
    }

    @Test
    void fetchData_shouldCalculateSpread_andIncludeInOutput() {
        Map<String, Object> apiResponse = Map.of(
                "rates", Map.of("USD", 15000.0, "EUR", 16000.0)
        );
        when(restTemplate.getForObject(anyString(), Mockito.eq(Map.class))).thenReturn(apiResponse);
        
        List<Map<String, Object>> data = fetcher.fetchData();
        assertThat(data).isNotEmpty();
        Map<String, Object> usdEntry = data.stream()
                .filter(m -> "USD".equals(m.get("currency")))
                .findFirst()
                .orElseThrow();
        assertThat(usdEntry.get("USD_BuySpread_IDR")).isNotNull();
    }
}