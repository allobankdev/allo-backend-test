package com.allobank.backend.runner;

import com.allobank.backend.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FinanceDataStartupRunnerTest {

    @Autowired
    private FinanceDataStore dataStore;

    @Test
    void testApplicationRunnerPopulatesStoreOnStartup() {
       
        
        assertNotNull(dataStore.getData("latest_idr_rates"), "Data latest_idr_rates harusnya sudah ada di memori");
        assertNotNull(dataStore.getData("historical_idr_usd"), "Data historical_idr_usd harusnya sudah ada di memori");
        assertNotNull(dataStore.getData("supported_currencies"), "Data supported_currencies harusnya sudah ada di memori");
    }
}