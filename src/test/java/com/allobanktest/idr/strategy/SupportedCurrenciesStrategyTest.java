package com.allobanktest.idr.strategy;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesStrategyTest {

    private static MockWebServer mockWebServer;
    private SupportedCurrenciesStrategy strategy;

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
        strategy = new SupportedCurrenciesStrategy(webClient);
    }

    @Test
    void fetchData_returnsCurrenciesMap() {
        String json = """
                {
                  "USD": "United States Dollar",
                  "IDR": "Indonesian Rupiah"
                }
                """;

        mockWebServer.enqueue(new MockResponse().setBody(json).addHeader("Content-Type", "application/json"));

        Mono<Map<String, Object>> mono = strategy.fetchData();
        Map<String, Object> result = mono.block();

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result.get("currencies");
        assertNotNull(currencies);
        assertEquals("Indonesian Rupiah", currencies.get("IDR"));
        assertEquals("United States Dollar", currencies.get("USD"));
    }
}
