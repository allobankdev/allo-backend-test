package org.allobanktest.runner;

import org.allobanktest.store.FinancialDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StartupDataLoaderIntegrationTest {

    @Autowired
    private FinancialDataStore store;

    @Test
    void testDataLoadedOnStartup() {
        assertNotNull(store.getLatestIdrRates());
        assertFalse(store.getLatestIdrRates().isEmpty());
        assertNotNull(store.getHistoricalIdrUsd());
        assertFalse(store.getHistoricalIdrUsd().isEmpty());
        assertNotNull(store.getSupportedCurrencies());
        assertFalse(store.getSupportedCurrencies().isEmpty());
    }
}
