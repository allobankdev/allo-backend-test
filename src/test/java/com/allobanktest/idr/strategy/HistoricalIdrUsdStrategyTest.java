package com.allobanktest.idr.strategy;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrUsdStrategyTest {

    private static MockWebServer mockWebServer;
    private HistoricalIdrUsdStrategy strategy;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        strategy = new HistoricalIdrUsdStrategy(webClient);
    }

    @Test
    void fetchData_returnsTimeSeriesMappedCorrectly() {
        String json = """
                {
                  "amount": 1,
                  "base": "IDR",
                  "start_date": "2023-12-29",
                  "end_date": "2024-01-05",
                  "rates": {
                    "2023-12-29": { "USD": 0.000065 },
                    "2024-01-02": { "USD": 0.000064 }
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse().setBody(json).addHeader("Content-Type", "application/json"));

        Mono<Map<String, Object>> mono = strategy.fetchData();
        Map<String, Object> result = mono.block();

        assertNotNull(result);
        assertEquals(1, result.get("amount"));
        assertEquals("IDR", result.get("base"));
        assertEquals("2023-12-29", result.get("startDate"));
        assertEquals("2024-01-05", result.get("endDate"));

        @SuppressWarnings("unchecked")
        Map<String, Map<String, BigDecimal>> rates = (Map<String, Map<String, BigDecimal>>) result.get("rates");
        assertNotNull(rates);
        assertTrue(rates.containsKey("2023-12-29"));
        assertEquals(new BigDecimal("0.000065"), rates.get("2023-12-29").get("USD"));
    }
}
