package com.test.allo_bank_test_exhange_rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.test.allo_bank_test_exhange_rate.service.SupportedCurrenciesFetcher;

public class SupportedCurrenciesFetcherTest {
    
    @Test
    void testFetchData() {
        WebClient webClient = WebClient.builder().baseUrl("https://api.frankfurter.dev/v1").defaultHeader("Accept", "application/json").build();
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(webClient);
        Map<String, String> resultMap = (Map<String, String>) fetcher.fetchData().block();
        assertEquals("United States Dollar", resultMap.get("USD"));
    }
}
