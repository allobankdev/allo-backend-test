package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.HistoricalRateItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIDRUSDFetcherTest {

    private MockWebServer mockWebServer;
    private HistoricalIDRUSDFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        fetcher = new HistoricalIDRUSDFetcher(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchAndTransform_returnsHistoricalRatesSortedByDate() {
        String json = """
                {
                  "base": "IDR",
                  "start_date": "2024-01-01",
                  "end_date": "2024-01-05",
                  "rates": {
                    "2024-01-03": { "USD": 0.0000642 },
                    "2024-01-02": { "USD": 0.0000638 },
                    "2024-01-04": { "USD": 0.0000645 }
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        List<HistoricalRateItem> result = fetcher.fetchAndTransform();

        assertEquals(3, result.size());
        assertEquals("2024-01-02", result.get(0).date());
        assertEquals("2024-01-03", result.get(1).date());
        assertEquals("2024-01-04", result.get(2).date());
        assertEquals(new BigDecimal("0.0000642"), result.get(1).rates().get("USD"));
    }

    @Test
    void getResourceType_returnsCorrectType() {
        assertEquals("historical_idr_usd", fetcher.getResourceType());
    }
}
