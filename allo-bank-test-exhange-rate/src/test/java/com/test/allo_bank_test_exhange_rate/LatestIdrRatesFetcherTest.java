package com.test.allo_bank_test_exhange_rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.test.allo_bank_test_exhange_rate.service.LatestIdrRatesFetcher;

public class LatestIdrRatesFetcherTest {

    @Test
    void testFetchData() {
        WebClient webClient = WebClient.builder().baseUrl("https://api.frankfurter.dev/v1").defaultHeader("Accept", "application/json").build();
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(webClient);
        fetcher.setGithubUsername("Schwanzeirs");
        Map<String, Object> resultMap = (Map<String, Object>) fetcher.fetchData().block();
        assertEquals(true, resultMap.containsKey("computed"));
    }
}
