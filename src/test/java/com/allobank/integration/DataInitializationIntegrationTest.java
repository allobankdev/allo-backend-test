package com.allobank.integration;

import com.allobank.service.InMemoryDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "frankfurter.api.base-url=https://api.frankfurter.app",
        "github.username=testuser"
})
class DataInitializationIntegrationTest {

    @Autowired
    private InMemoryDataStore dataStore;

    @Test
    void testDataStoreIsInitialized() {
        // This test verifies that the ApplicationRunner has executed
        // and loaded data into the store
        assertNotNull(dataStore);
        assertTrue(dataStore.isDataLoaded(), "Data should be loaded after ApplicationRunner execution");
    }

    @Test
    void testAllResourceTypesAreLoaded() {
        // Verify that all three resource types are available
        assertNotNull(dataStore.getData("latest_idr_rates"), "Latest IDR rates should be loaded");
        assertNotNull(dataStore.getData("historical_idr_usd"), "Historical IDR to USD should be loaded");
        assertNotNull(dataStore.getData("supported_currencies"), "Supported currencies should be loaded");
    }
}

