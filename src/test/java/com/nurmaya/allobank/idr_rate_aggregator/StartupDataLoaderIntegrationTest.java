package com.nurmaya.allobank.idr_rate_aggregator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.nurmaya.allobank.idr_rate_aggregator.service.AggregatedDataStore;
import com.nurmaya.allobank.idr_rate_aggregator.strategy.IDRDataFetcher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StartupDataLoaderIntegrationTest {

    @Autowired
    private AggregatedDataStore dataStore;

    @Autowired
    private List<IDRDataFetcher> fetchers;

    @Test
    void testStartupDataLoader_ShouldLoadAllResources() {
        assertNotNull(dataStore);

        // Pastikan ketiga resource sudah masuk ke dataStore
        assertTrue(dataStore.containsKey("latest_idr_rates"));
        assertTrue(dataStore.containsKey("historical_idr_usd"));
        assertTrue(dataStore.containsKey("supported_currencies"));

        assertEquals(3, fetchers.size());
    }
}
