package com.exchange.frankfurter.unit.strategy;

import com.exchange.frankfurter.strategy.HistoricalIdrUsdFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalIdrUsdFetcherTest {

    private static MockWebServer mockWebServer;
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testFetchHistoricalData() throws InterruptedException {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        fetcher = new HistoricalIdrUsdFetcher(webClient);

        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"rates\": {\"2024-01-01\": {\"USD\": 0.000065}}}")
                .addHeader("Content-Type", "application/json"));

        Object result = fetcher.fetchData();

        assertThat(result).isNotNull();
        // Memastikan request dikirim ke URI yang benar
        assertThat(mockWebServer.takeRequest().getPath())
                .isEqualTo("/2024-01-01..2024-01-05?from=IDR&to=USD");
    }
}
