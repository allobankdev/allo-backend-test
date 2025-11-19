package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.HistoricalRatesData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIDRUSDStrategyTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private HistoricalIDRUSDStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new HistoricalIDRUSDStrategy();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
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
                        "2024-01-02": {"USD": 0.000064},
                        "2024-01-03": {"USD": 0.000064}
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Object result = strategy.fetchData(webClient);

        assertInstanceOf(HistoricalRatesData.class, result);
        HistoricalRatesData data = (HistoricalRatesData) result;

        assertEquals(1.0, data.getAmount());
        assertEquals("IDR", data.getBase());
        assertEquals("2024-01-01", data.getStartDate());
        assertEquals("2024-01-05", data.getEndDate());
        assertNotNull(data.getRates());
        assertEquals(2, data.getRates().size());
        assertEquals(0.000064, data.getRates().get("2024-01-02").get("USD"));
    }
}
