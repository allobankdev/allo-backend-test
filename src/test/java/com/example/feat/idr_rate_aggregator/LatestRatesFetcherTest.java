package com.example.feat.idr_rate_aggregator;

import com.example.feat.idr_rate_aggregator.dto.LatestRatesResponse;
import com.example.feat.idr_rate_aggregator.service.Latest.LatestRatesFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class LatestRatesFetcherTest {

    public static MockWebServer mockBackEnd;
    private LatestRatesFetcher latestRatesFetcher;

    private static final String MOCK_USERNAME = "mendochandra";
    private static final BigDecimal EXPECTED_SPREAD_FACTOR = new BigDecimal("0.00252");

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

        latestRatesFetcher = new LatestRatesFetcher(webClient, MOCK_USERNAME);
    }

    @Test
    void testSpreadFactorCalculation() {
        assertEquals(EXPECTED_SPREAD_FACTOR.setScale(5, RoundingMode.HALF_UP), latestRatesFetcher.spreadFactor.setScale(5, RoundingMode.HALF_UP), "Spread Factor harus 0.00685 untuk fajar4u.");
    }

    @Test
    void testFetchData_SuccessAndTransformation() throws InterruptedException {
        BigDecimal apiRateUsd = new BigDecimal("0.000060");

        BigDecimal expectedBuySpread = new BigDecimal("16780.8333");

        String mockResponse = "{\"amount\": 1,\"base\": \"IDR\",\"date\": \"2024-11-20\","
                + "\"rates\": {\"AUD\": 0.000092,\"USD\": " + apiRateUsd.toPlainString() + "}}";

        mockBackEnd.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        LatestRatesResponse result = (LatestRatesResponse) latestRatesFetcher.fetchData();

        assertNotNull(result, "Hasil tidak boleh null");

        assertTrue(result.getRates().containsKey("USD"), "Field USD dari API harus ada");

        assertNotNull(result.getUSDBuySpreadIDR(), "Field USDBuySpreadIDR tidak boleh null setelah perhitungan");

        BigDecimal actualSpread = result.getUSDBuySpreadIDR();

        assertEquals(expectedBuySpread, actualSpread.setScale(4, RoundingMode.HALF_UP), "Perhitungan Spread tidak sesuai");

        assertEquals("/latest?base=IDR", mockBackEnd.takeRequest().getPath(), "URI panggilan harus '/latest?base=IDR'");
    }

}