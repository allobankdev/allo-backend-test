package cory.sakti.Financial.integration;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        ResponseEntity<Object> latestResponse = restTemplate.getForEntity("/api/finance/latest_idr_rates", Object.class);
        assertEquals(HttpStatus.OK, latestResponse.getStatusCode());

        // 3. Test 'supported_currencies'
        ResponseEntity<Map> currencyResponse = restTemplate.getForEntity("/api/finance/supported_currencies", Map.class);
        assertEquals(HttpStatus.OK, currencyResponse.getStatusCode());
        assertTrue(currencyResponse.getBody().containsKey("USD"));

        // 4.  Test 'historical_idr_usd'
        ResponseEntity<Map> historicalResponse = restTemplate.getForEntity("/api/finance/historical_idr_usd", Map.class);

        assertEquals(HttpStatus.OK, historicalResponse.getStatusCode());
        assertNotNull(historicalResponse.getBody());

        assertTrue(historicalResponse.getBody().containsKey("2023-12-29"),
                "Historical data for 2023-12-29 is missing");
    }
}
