package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.config.ApplicationProperties;
import com.allobank.idr_rate_aggregator.model.dto.LatestIDRRatesResponse;
import com.allobank.idr_rate_aggregator.util.SpreadCalculator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LatestIDRRatesStrategyTest {

    private MockWebServer mockWebServer;
    private LatestIDRRatesStrategy strategy;
    private SpreadCalculator spreadCalculator;
    private ApplicationProperties properties;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        
        spreadCalculator = new SpreadCalculator();
        properties = new ApplicationProperties();
        properties.setGithubUsername("testuser");
        
        strategy = new LatestIDRRatesStrategy(webClient, spreadCalculator, properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testFetchData_Success() {
        // Mock API response
        String mockResponse = """
            {
                "amount": 1.0,
                "base": "IDR",
                "date": "2024-01-22",
                "rates": {
                    "USD": 0.000063,
                    "EUR": 0.000058,
                    "GBP": 0.000050
                }
            }
            """;
        
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        
        // Execute
        Object result = strategy.fetchData();
        
        // Verify
        assertNotNull(result);
        assertTrue(result instanceof LatestIDRRatesResponse);
        
        LatestIDRRatesResponse response = (LatestIDRRatesResponse) result;
        assertEquals("IDR", response.getBase());
        assertEquals("2024-01-22", response.getDate());
        assertNotNull(response.getUsdBuySpreadIdr());
        assertEquals("testuser", response.getGithubUsername());
        assertTrue(response.getUsdBuySpreadIdr() > 0);
    }

    @Test
    void testGetResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }

    @Test
    void testFetchData_MissingUsdRate() {
        // Mock response without USD
        String mockResponse = """
            {
                "amount": 1.0,
                "base": "IDR",
                "date": "2024-01-22",
                "rates": {
                    "EUR": 0.000058
                }
            }
            """;
        
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        
        // Should throw exception
        assertThrows(RuntimeException.class, () -> strategy.fetchData());
    }

    @Test
    void testFetchData_NetworkError() {
        // Mock server error
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        
        // Should throw exception
        assertThrows(RuntimeException.class, () -> strategy.fetchData());
    }

    @Test
    void testSpreadCalculation() {
        // Mock successful response
        String mockResponse = """
            {
                "amount": 1.0,
                "base": "IDR",
                "date": "2024-01-22",
                "rates": {
                    "USD": 0.000063
                }
            }
            """;
        
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));
        
        // Execute
        LatestIDRRatesResponse response = (LatestIDRRatesResponse) strategy.fetchData();
        
        // Verify spread is calculated
        assertNotNull(response.getUsdBuySpreadIdr());
        
        // Spread should make IDR rate higher than base rate
        double baseRate = 1.0 / 0.000063;
        assertTrue(response.getUsdBuySpreadIdr() > baseRate);
    }
}

