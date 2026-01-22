package com.allobank.idr_rate_aggregator.service.strategy;
import com.allobank.idr_rate_aggregator.config.FrankfurterApiProperties;
import com.allobank.idr_rate_aggregator.model.dto.FrankfurterTimeSeriesResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HistoricalIDRUSDStrategy
 */
class HistoricalIDRUSDStrategyTest {

    private MockWebServer mockWebServer;
    private HistoricalIDRUSDStrategy strategy;
    private FrankfurterApiProperties properties;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        properties = new FrankfurterApiProperties();
        properties.getHistorical().setStartDate("2024-01-01");
        properties.getHistorical().setEndDate("2024-01-05");

        strategy = new HistoricalIDRUSDStrategy(webClient, properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testFetchData_Success() {
        String mockResponse = """
                {
                    "amount": 1.0,
                    "base": "IDR",
                    "start_date": "2024-01-01",
                    "end_date": "2024-01-05",
                    "rates": {
                        "2024-01-01": {"USD": 0.000064},
                        "2024-01-02": {"USD": 0.000065},
                        "2024-01-03": {"USD": 0.000064},
                        "2024-01-04": {"USD": 0.000063},
                        "2024-01-05": {"USD": 0.000064}
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Object result = strategy.fetchData();

        assertNotNull(result);
        assertTrue(result instanceof FrankfurterTimeSeriesResponse);

        FrankfurterTimeSeriesResponse response = (FrankfurterTimeSeriesResponse) result;
        assertEquals("IDR", response.getBase());
        assertEquals("2024-01-01", response.getStartDate());
        assertEquals("2024-01-05", response.getEndDate());
        assertNotNull(response.getRates());
        assertEquals(5, response.getRates().size());
    }

    @Test
    void testFetchData_NetworkError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(RuntimeException.class, () -> strategy.fetchData());
    }

    @Test
    void testGetResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
    }
}
