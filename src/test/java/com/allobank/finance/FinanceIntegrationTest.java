package com.allobank.finance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.allobank.finance.runner.IDRDataInitializationRunner;
import com.allobank.finance.store.IDRDataStore;

@SpringBootTest
public class FinanceIntegrationTest {

    @Autowired
    private IDRDataInitializationRunner initializationRunner;

    @Autowired
    private IDRDataStore dataStore;

    @BeforeEach
    public void setup() throws Exception {
        initializationRunner.run(null);
    }

    @Test
    public void cachedDataIsPresent() {
        assert dataStore.contains("latest_idr_rates");
        assert dataStore.contains("historical_idr_usd");
        assert dataStore.contains("supported_currencies");
    }
}
