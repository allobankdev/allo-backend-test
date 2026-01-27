package com.backend.allobank.strategy;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesStrategyTest {

    private static MockWebServer mockWebServer;
    private SupportedCurrenciesStrategy strategy;

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

        strategy = new SupportedCurrenciesStrategy(webClient);
    }

    @Test
    void shouldReturnCurrenciesMap() {
        String json = """
            {
              "USD": "United States Dollar",
              "IDR": "Indonesian Rupiah"
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
        Map<String, String> map = (Map<String, String>) result;

        assertEquals("United States Dollar", map.get("USD"));
        assertEquals("Indonesian Rupiah", map.get("IDR"));
    }
}
