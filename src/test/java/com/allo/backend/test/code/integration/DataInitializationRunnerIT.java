package com.allo.backend.test.code.integration;

import com.allo.backend.test.code.service.DataStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataInitializationRunnerIT {

    @Autowired
    private DataStorageService dataStorageService;

    @Test
    void testApplicationStartup_LoadsData() {
        // After application startup, data should be initialized
        assertTrue(dataStorageService.isInitialized());

        // Verify all three resources are loaded
        assertDoesNotThrow(() -> dataStorageService.getData("latest_idr_rates"));
        assertDoesNotThrow(() -> dataStorageService.getData("historical_idr_usd"));
        assertDoesNotThrow(() -> dataStorageService.getData("supported_currencies"));
    }

    @Test
    void testDataStore_IsImmutable() {
        // Data store should be initialized and immutable
        assertTrue(dataStorageService.isInitialized());

        // Attempting to store more data should fail
        assertThrows(IllegalStateException.class,
                () -> dataStorageService.storeData("new_resource", "new_data"));
    }
}
