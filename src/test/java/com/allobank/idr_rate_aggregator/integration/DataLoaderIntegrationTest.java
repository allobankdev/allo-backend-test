package com.allobank.idr_rate_aggregator.integration;

import com.allobank.idr_rate_aggregator.service.DataCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test to verify ApplicationRunner successfully loads data on startup.
 * This test ensures that the data cache is properly initialized before the application
 * context is fully ready.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "app.github-username=test-user",
        "frankfurter.api.base-url=https://api.frankfurter.app"
})
class DataLoaderIntegrationTest {

    @Autowired
    private DataCacheService dataCacheService;

    @Test
    void testDataLoadedOnStartup() {
        // Verify all three resource types are loaded
        assertTrue(dataCacheService.isDataLoaded("latest_idr_rates"),
                "Latest IDR rates should be loaded on startup");
        
        assertTrue(dataCacheService.isDataLoaded("historical_idr_usd"),
                "Historical IDR to USD data should be loaded on startup");
        
        assertTrue(dataCacheService.isDataLoaded("supported_currencies"),
                "Supported currencies should be loaded on startup");
    }

    @Test
    void testSupportedResourceTypes() {
        var supportedTypes = dataCacheService.getSupportedResourceTypes();
        
        assertEquals(3, supportedTypes.size(), "Should have exactly 3 supported resource types");
        assertTrue(supportedTypes.contains("latest_idr_rates"));
        assertTrue(supportedTypes.contains("historical_idr_usd"));
        assertTrue(supportedTypes.contains("supported_currencies"));
    }

    @Test
    void testGetDataReturnsNonNull() {
        // Verify that getData returns non-null for all resource types
        assertNotNull(dataCacheService.getData("latest_idr_rates"));
        assertNotNull(dataCacheService.getData("historical_idr_usd"));
        assertNotNull(dataCacheService.getData("supported_currencies"));
    }

    @Test
    void testGetDataThrowsExceptionForUnsupportedType() {
        assertThrows(IllegalArgumentException.class, () -> {
            dataCacheService.getData("invalid_resource_type");
        });
    }
}
