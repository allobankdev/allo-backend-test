package com.allo.backendtest.integration;

import com.allo.backendtest.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class StartupDataLoaderIntegrationTest {

    @Autowired
    private FinanceDataStore store;

    @Test
    void shouldLoadDataOnStartup() {
        assertTrue(store.contains("latest_idr_rates"));
        assertTrue(store.contains("historical_idr_usd"));
        assertTrue(store.contains("supported_currencies"));
    }
}