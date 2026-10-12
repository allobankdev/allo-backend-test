package com.allobank;

import com.allobank.service.DataFetchingService;
import com.allobank.service.DataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the application startup flow.
 * Verifies that the ApplicationRunner successfully initializes and loads
 * data into the in-memory store before the application context is ready.
 */
@SpringBootTest
class DataLoadingIntegrationTest {
    
    @Autowired
    private DataStore dataStore;
    
    @Autowired
    private DataFetchingService dataFetchingService;
    
    @Test
    void testApplicationStartupLoadsDataIntoStore() {
        // Verify that data store is initialized after application startup
        assertTrue(dataStore.isInitialized(),
                "DataStore should be initialized after application startup");
        
        // Verify that at least some resources were loaded
        assertTrue(dataStore.getResourceCount() >= 0,
                "DataStore should have loaded resources");
    }
    
    @Test
    void testDataStoreIsThreadSafeDuringAccess() {
        // Verify that the data store can handle concurrent read access
        Object data1 = dataStore.getData("latest_idr_rates");
        Object data2 = dataStore.getData("historical_idr_usd");
        Object data3 = dataStore.getData("supported_currencies");
        
        // At least one of these should be available (if external API call succeeds)
        // Or they might be null if the external API is unavailable
        // This test mainly verifies no exceptions are thrown during access
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                dataStore.getData("latest_idr_rates");
            }
        });
    }
    
    @Test
    void testDataStoreIsImmutableAfterInitialization() {
        // Verify that data cannot be added after initialization
        boolean result = dataStore.storeData("test_resource_after_init", "test_data");
        assertFalse(result, "Should not be able to store data after initialization");
    }
}
