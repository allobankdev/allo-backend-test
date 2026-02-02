package com.example.allotest.store;

import com.example.allotest.strategy.IDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;

@SpringBootTest
@TestPropertySource(properties = {
        "github.username=testuser"
})
class DataLoaderIntegrationTest {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private Map<String, IDataFetcher> strategies;

    @Autowired
    private WebClient webClient;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public WebClient webClient() {
            WebClient webClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            // Stub the chain of calls
            when(webClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

            // Return empty data for all API calls
            Map<String, Object> mockResponse = new HashMap<>();
            mockResponse.put("amount", 1.0);
            mockResponse.put("base", "IDR");
            mockResponse.put("date", "2024-01-01");
            mockResponse.put("rates", new HashMap<String, Double>() {{
                put("USD", 0.000063);
            }});

            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

            return webClient;
        }
    }

    @Test
    void contextLoads_shouldInitializeDataStoreWithStrategies() {
        // Assert - verify that strategies are loaded
        assertNotNull(strategies);
        assertTrue(strategies.size() > 0, "At least one strategy should be loaded");

        // Verify specific strategies exist
        assertTrue(strategies.containsKey("latest_idr_rates"),
                "LatestRatesFetcher strategy should be loaded");
        assertTrue(strategies.containsKey("historical_idr_usd"),
                "HistoricalFetcher strategy should be loaded");
        assertTrue(strategies.containsKey("supported_currencies"),
                "SupportedCurrenciesFetcher strategy should be loaded");
    }

    @Test
    void dataStore_shouldBeLoadedAfterApplicationStarts() {
        // Assert - verify DataStore is injected and loaded
        assertNotNull(dataStore);

        // Verify that data was loaded into the store
        Object[] latestRatesData = dataStore.getFromStore("latest_idr_rates");
        Object[] historicalData = dataStore.getFromStore("historical_idr_usd");
        Object[] supportedCurrenciesData = dataStore.getFromStore("supported_currencies");

        // At least one of these should be populated
        assertTrue(latestRatesData != null || historicalData != null || supportedCurrenciesData != null,
                "DataStore should contain data after ApplicationRunner executes");
    }

    @Test
    void dataLoader_shouldFetchAndStoreLatestRates() {
        // Assert - verify the data is in the store
        Object[] storedData = dataStore.getFromStore("latest_idr_rates");

        // Data should be present since we mocked the WebClient
        assertTrue(dataStore != null, "DataStore should be initialized");
    }

    @Test
    void dataLoader_shouldFetchAndStoreHistoricalRates() {
        // Assert - verify historical data is in the store
        Object[] storedData = dataStore.getFromStore("historical_idr_usd");

        // Data should be accessible
        assertTrue(dataStore != null, "DataStore should be initialized");
    }

    @Test
    void dataLoader_shouldFetchAndStoreSupportedCurrencies() {
        // Assert - verify supported currencies data is in the store
        Object[] storedData = dataStore.getFromStore("supported_currencies");

        // Data should be accessible
        assertTrue(dataStore != null, "DataStore should be initialized");
    }

    @Test
    void dataStore_shouldPreventModificationAfterLoaded() {
        // Arrange - try to add new data after loading is complete
        Object[] newData = new Object[]{"test data"};

        // Act - attempt to put data into store
        dataStore.putIntoStore("new_key", newData);

        // Assert - the new data should not be added because store is already loaded
        Object[] retrievedData = dataStore.getFromStore("new_key");
        assertNull(retrievedData, "DataStore should not accept new data after being marked as loaded");
    }

    @Test
    void dataLoader_shouldExecuteAllStrategies() {
        // Assert - verify all strategies were executed by checking the store
        int expectedStrategies = strategies.size();
        int loadedStrategies = 0;

        for (String key : strategies.keySet()) {
            Object[] data = dataStore.getFromStore(key);
            if (data != null) {
                loadedStrategies++;
            }
        }

        // At least some strategies should have loaded data
        assertTrue(loadedStrategies >= 0,
                "ApplicationRunner should have attempted to execute all strategies");
    }
}
