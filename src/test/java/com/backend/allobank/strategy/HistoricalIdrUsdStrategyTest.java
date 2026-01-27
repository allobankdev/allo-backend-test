package com.backend.allobank.strategy;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrUsdStrategyTest {

    private static MockWebServer mockWebServer;
    private HistoricalIdrUsdStrategy strategy;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        strategy = new HistoricalIdrUsdStrategy(webClient);
    }

    @Test
    void shouldReturnHistoricalRates() {
        String json = """
            {
              "rates": {
                "2024-01-01": { "USD": 0.000063 },
                "2024-01-02": { "USD": 0.000064 }
              }
            }
            """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(json)
                        .addHeader("Content-Type", "application/json")
        );

        Object result = strategy.fetchAndTransform();
        assertNotNull(result);

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) result;

        assertTrue(map.containsKey("rates"));
    }
}
