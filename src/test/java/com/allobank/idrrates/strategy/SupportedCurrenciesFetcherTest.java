package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.CurrencyItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesFetcherTest {

    private MockWebServer mockWebServer;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchAndTransform_returnsCurrenciesSortedByCode() {
        String json = """
                {
                  "USD": "United States Dollar",
                  "EUR": "Euro",
                  "IDR": "Indonesian Rupiah"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        List<CurrencyItem> result = fetcher.fetchAndTransform();

        assertEquals(3, result.size());
        assertEquals("EUR", result.get(0).code());
        assertEquals("Euro", result.get(0).name());
        assertEquals("IDR", result.get(1).code());
        assertEquals("Indonesian Rupiah", result.get(1).name());
        assertEquals("USD", result.get(2).code());
    }

    @Test
    void getResourceType_returnsCorrectType() {
        assertEquals("supported_currencies", fetcher.getResourceType());
    }
}
