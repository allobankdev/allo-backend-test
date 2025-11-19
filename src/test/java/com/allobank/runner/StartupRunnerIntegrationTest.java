package com.allobank.runner;

import com.allobank.service.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StartupRunnerIntegrationTest {

    @Autowired
    private InMemoryDataStore store;

    @Test
    void data_is_loaded_at_startup() {
        assertNotNull(store.get("latest_idr_rates"), "Latest IDR Rates must be loaded");
        assertNotNull(store.get("historical_idr_usd"), "Historical must be loaded");
        assertNotNull(store.get("supported_currencies"), "Currencies must be loaded");
    }
}
