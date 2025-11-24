package com.test.allo_bank_test_exhange_rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.test.allo_bank_test_exhange_rate.service.HistoricalIdrUsdFetcher;

public class HistoricalIdrUsdFetcherTest {
    
    @Test
    void testFetchData() {
        WebClient webClient = WebClient.builder().baseUrl("https://api.frankfurter.dev/v1").defaultHeader("Accept", "application/json").build();
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(webClient);
        Map<String, Object> resultMap = (Map<String, Object>) fetcher.fetchData().block();
        assertEquals("2024-01-05", (String) resultMap.get("end_date"));
    }
}
