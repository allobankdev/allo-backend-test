package com.example.finance.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.finance.client.FrankfurterClient;
import com.example.finance.service.DataInitializationService;
import com.example.finance.service.InMemoryDataStore;

@SpringBootTest
@ActiveProfiles("test")
class DataLoaderIntegrationTest {

    @Autowired
    private DataInitializationService initService;

    @Autowired
    private InMemoryDataStore dataStore;

    @MockBean
    private FrankfurterClient client;

    @Test
    void testDataLoadedOnStartup() {

        // MOCK RESPONSE
        when(client.getLatestRates()).thenReturn("{\"rates\":{\"USD\":0.000064}}");

        when(client.getHistoricalRates()).thenReturn(
            "{\"rates\":{\"2024-01-01\":{\"USD\":0.000064}}}"
        );

        when(client.getCurrencies()).thenReturn(
            "{\"USD\":\"United States Dollar\"}"
        );

        // load data
        initService.loadAllData();

        assertNotNull(dataStore.get("latest_idr_rates"));
        assertNotNull(dataStore.get("historical_idr_usd"));
        assertNotNull(dataStore.get("supported_currencies"));
    }
}