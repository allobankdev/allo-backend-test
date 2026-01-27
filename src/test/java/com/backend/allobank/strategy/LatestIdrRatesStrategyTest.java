package com.backend.allobank.strategy;

import com.backend.allobank.util.SpreadFactorCalculator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesStrategyTest {

    private static MockWebServer mockWebServer;
    private LatestIdrRatesStrategy strategy;

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

        strategy = new LatestIdrRatesStrategy(webClient);
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        String json = """
            {
              "base": "IDR",
              "date": "2024-01-05",
              "rates": {
                "USD": 0.000064
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

        Double rateUsd = ((Map<String, Double>) map.get("rates")).get("USD");
        Double actualSpread = (Double) map.get("USD_BuySpread_IDR");

        double spreadFactor = SpreadFactorCalculator.calculate("muhammadakbaar");
        double expected = (1 / rateUsd) * (1 + spreadFactor);

        assertEquals(expected, actualSpread, 1e-9);
        assertEquals("IDR", map.get("base"));
        assertEquals("2024-01-05", map.get("date"));
    }
}

