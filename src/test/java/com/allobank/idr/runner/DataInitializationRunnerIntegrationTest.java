package com.allobank.idr.runner;

import com.allobank.idr.service.DataStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "github.username=testuser"
})
class DataInitializationRunnerIntegrationTest {

    @Autowired
    private DataStoreService dataStoreService;

    @Test
    void shouldInitializeAllDataOnStartup() {
        assertTrue(dataStoreService.isInitialized(), "Data store should be initialized");
        
        assertNotNull(dataStoreService.getData("latest_idr_rates"), 
            "Latest IDR rates should be loaded");
        assertNotNull(dataStoreService.getData("historical_idr_usd"), 
            "Historical IDR USD should be loaded");
        assertNotNull(dataStoreService.getData("supported_currencies"), 
            "Supported currencies should be loaded");
    }

    @Test
    void shouldContainSpreadFactorInLatestRates() {
        var latestRates = dataStoreService.getData("latest_idr_rates");
        
        assertTrue(latestRates.containsKey("USD_BuySpread_IDR"), 
            "Latest rates should contain USD_BuySpread_IDR");
        assertTrue(latestRates.containsKey("spread_factor"), 
            "Latest rates should contain spread_factor");
    }

    @Test
    void shouldHaveImmutableData() {
        var data = dataStoreService.getData("latest_idr_rates");
        
        assertThrows(UnsupportedOperationException.class, () -> {
            data.put("test", "value");
        }, "Data should be immutable");
    }
}
