package com.allobank.finance.runner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.allobank.finance.store.FinanceDataStore;

@SpringBootTest
@ActiveProfiles("test")
class FinanceDataLoaderIT {

    @Autowired
    FinanceDataStore dataStore;

    @Test
    void shouldLoadAllDataOnStartup() {
        assertNotNull(dataStore.get("latest_idr_rates"));
        assertNotNull(dataStore.get("historical_idr_usd"));
        assertNotNull(dataStore.get("supported_currencies"));
    }
}