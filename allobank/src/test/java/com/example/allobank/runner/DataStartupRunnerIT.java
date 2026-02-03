package com.example.allobank.runner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.allobank.cache.ExchangeRateCache;


@SpringBootTest
class DataStartupRunnerIT {

    @Autowired
    private ExchangeRateCache cache;

    @Test
    void shouldLoadAllResourcesAtStartup() {

        assertNotNull(cache.get("latest_idr_rates"));
        assertNotNull(cache.get("historical_idr_usd"));
        assertNotNull(cache.get("supported_currencies"));
    }
}

