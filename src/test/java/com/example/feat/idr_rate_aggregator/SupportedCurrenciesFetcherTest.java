package com.example.feat.idr_rate_aggregator;

import com.example.feat.idr_rate_aggregator.service.Currencies.SupportedCurrenciesFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SupportedCurrenciesFetcherTest {

    public static MockWebServer mockBackEnd;
    private SupportedCurrenciesFetcher supportedCurrenciesFetcher;

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
        supportedCurrenciesFetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @Test
    void testFetchData_SuccessAndContentCheck() throws InterruptedException {
        // 1. Siapkan respons sukses (Map JSON)
        String mockResponse = "{\"AUD\": \"Australian Dollar\", \"IDR\": \"Indonesian Rupiah\", \"USD\": \"United States Dollar\"}";

        mockBackEnd.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Map<String, String> result = (Map<String, String>) supportedCurrenciesFetcher.fetchData();

        assertNotNull(result, "Hasil tidak boleh null");
        assertTrue(result.containsKey("IDR"), "List harus mengandung IDR");
        assertEquals("Indonesian Rupiah", result.get("IDR"), "Deskripsi Rupiah harus benar");

        String actualPath = mockBackEnd.takeRequest().getPath();
        assertEquals("/currencies", actualPath, "URI panggilan tidak sesuai dengan /currencies");
    }

}