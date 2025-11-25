package com.example.financedata.integration;

import com.example.financedata.store.ImmutableFinanceStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StartupDataLoaderIntegrationTest {

    @Autowired
    ImmutableFinanceStore store;

    @Test
    public void testDataLoadedOnStartup() {
        assertTrue(store.isLoaded(), "Store should be loaded at startup");
        assertNotNull(store.get("latest_idr_rates"));
        assertNotNull(store.get("historical_idr_usd"));
        assertNotNull(store.get("supported_currencies"));
    }
}
