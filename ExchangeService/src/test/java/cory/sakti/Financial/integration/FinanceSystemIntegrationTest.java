package cory.sakti.Financial.integration;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FinanceSystemIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InMemoryDataStoreService dataStore;

    @Test
    @DisplayName("Full System Integration Check (All Resources)")
    void systemShouldIngestAndServeAllData() {
        // 1. Verify the DataStore was locked by the Runner
        assertTrue(dataStore.isInitialized(), "System failed to seal data store on startup");

        // 2. Test 'latest_idr_rates'
        // Use List.class to match the Unified JSON Array requirement
        ResponseEntity<List> latestResponse = restTemplate.getForEntity("/api/finance/data/latest_idr_rates", List.class);
        assertEquals(HttpStatus.OK, latestResponse.getStatusCode());
        assertNotNull(latestResponse.getBody());
        assertFalse(latestResponse.getBody().isEmpty());

        // 3. Test 'supported_currencies'
        ResponseEntity<List> currencyResponse = restTemplate.getForEntity("/api/finance/data/supported_currencies", List.class);
        assertEquals(HttpStatus.OK, currencyResponse.getStatusCode());
        assertNotNull(currencyResponse.getBody());

        // Cast the first element to a Map to check keys
        Map<String, Object> currencyMap = (Map<String, Object>) currencyResponse.getBody().get(0);
        assertTrue(currencyMap.containsKey("USD"), "Supported currencies should contain USD");

        // 4. Test 'historical_idr_usd'
        ResponseEntity<List> historicalResponse = restTemplate.getForEntity("/api/finance/data/historical_idr_usd", List.class);
        assertEquals(HttpStatus.OK, historicalResponse.getStatusCode());
        assertNotNull(historicalResponse.getBody());
        assertFalse(historicalResponse.getBody().isEmpty());

        // Cast the first element to a Map to check the date
        Map<String, Object> historicalMap = (Map<String, Object>) historicalResponse.getBody().get(0);
        assertTrue(historicalMap.containsKey("2023-12-29"), "Historical data for 2023-12-29 is missing");
    }
}
