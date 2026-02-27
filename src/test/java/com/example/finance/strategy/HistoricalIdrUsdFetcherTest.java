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

class HistoricalIdrUsdFetcherTest {
    private RestTemplate restTemplate;
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new HistoricalIdrUsdFetcher(restTemplate);
    }

    @Test
    void fetchData_shouldReturnListWithDateAndRate() {
        Map<String, Object> apiResponse = Map.of(
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.00007),
                        "2024-01-02", Map.of("USD", 0.00008)
                )
        );
        when(restTemplate.getForObject(anyString(), Mockito.eq(Map.class))).thenReturn(apiResponse);
        List<Map<String, Object>> data = fetcher.fetchData();
        assertThat(data).hasSize(2);
        assertThat(data.get(0)).containsKeys("date", "rate");
    }
}