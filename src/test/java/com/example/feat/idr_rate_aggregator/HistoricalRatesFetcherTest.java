package com.example.feat.idr_rate_aggregator;

import com.example.feat.idr_rate_aggregator.dto.HistoricalRatesResponse;
import com.example.feat.idr_rate_aggregator.service.Historical.HistoricalRatesFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalRatesFetcherTest {

    public static MockWebServer mockBackEnd;
    private HistoricalRatesFetcher historicalRatesFetcher;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        WebClient webClient = WebClient.create(baseUrl);
        historicalRatesFetcher = new HistoricalRatesFetcher(webClient);
    }

    @Test
    void testFetchData_SuccessAndUriCheck() throws InterruptedException {
        String mockResponse = "{\"amount\": 1,\"base\": \"IDR\",\"start_date\": \"2024-01-01\",\"end_date\": \"2024-01-05\","
                + "\"rates\": {\"2024-01-02\": {\"USD\": 0.000064},\"2024-01-03\": {\"USD\": 0.000064}}}";

        mockBackEnd.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        HistoricalRatesResponse result = (HistoricalRatesResponse) historicalRatesFetcher.fetchData();

        assertNotNull(result, "Hasil tidak boleh null");
        assertTrue(result.getRates().containsKey("2024-01-02"), "Data tanggal harus ada");

        String actualPath = mockBackEnd.takeRequest().getPath();
        assertTrue(actualPath.startsWith("/2024-01-01..2024-01-05?from=IDR&to=USD"), "URI panggilan tidak sesuai dengan persyaratan soal.");
    }

}
