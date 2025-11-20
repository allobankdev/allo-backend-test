package com.example.allow.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.allow.strategy.IDRDataFetcher;

import reactor.core.publisher.Mono;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*; 

import java.time.Duration;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class StartupDataLoaderTest {

    @Autowired
    private DataAggregationService cache;

    @Autowired
    private List<IDRDataFetcher> fetchers;

    @Test
    void shouldLoadAllDataOnStartup() {
        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
            assertTrue(cache.isLoaded());
            assertNotNull(cache.get("latest_idr_rates"));
            assertNotNull(cache.get("historical_idr_usd"));
            assertNotNull(cache.get("supported_currencies"));
        });
    }
}
