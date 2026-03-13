package com.allobank.backend.test.startup;

import com.allobank.backend.test.model.DataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DataLoaderIntegrationTest {

    @Autowired
    private DataStore dataStore;

    @Test
    void verifyDataStoreIsPopulatedOnStartup() {

        assertAll("DataStore must contain all initial data from Frankfurter API",
                () -> assertNotNull(dataStore.getLatestRates(), "Latest rates should not be null"),
        () -> assertNotNull(dataStore.getCurrencies(), "Currencies should not be null"),
        () -> assertNotNull(dataStore.getHistoricalRates(), "Historical rates should not be null")
        );
    }
}