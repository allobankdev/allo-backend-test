package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.LatestRateItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LatestIDRRatesFetcherTest {

    private MockWebServer mockWebServer;
    private LatestIDRRatesFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        fetcher = new LatestIDRRatesFetcher(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchAndTransform_returnsRatesWithUsdSpread() {
        String json = """
                {
                  "base": "IDR",
                  "date": "2024-01-05",
                  "rates": {
                    "USD": 0.0000631,
                    "EUR": 0.0000574,
                    "GBP": 0.0000497
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        List<LatestRateItem> result = fetcher.fetchAndTransform();

        assertEquals(3, result.size());

        LatestRateItem usdItem = result.stream()
                .filter(i -> "USD".equals(i.currency()))
                .findFirst()
                .orElseThrow();

        assertNotNull(usdItem.usdBuySpreadIdr());

        // Verify spread calculation: (1 / 0.0000631) * (1 + 0.00333)
        BigDecimal idrPerUsd = BigDecimal.ONE.divide(new BigDecimal("0.0000631"), 10, RoundingMode.HALF_UP);
        BigDecimal spreadFactor = new BigDecimal("0.0033300000");
        BigDecimal expected = idrPerUsd.multiply(BigDecimal.ONE.add(spreadFactor)).setScale(4, RoundingMode.HALF_UP);
        assertEquals(expected, usdItem.usdBuySpreadIdr());

        // Non-USD currencies should not have spread
        LatestRateItem eurItem = result.stream()
                .filter(i -> "EUR".equals(i.currency()))
                .findFirst()
                .orElseThrow();
        assertNull(eurItem.usdBuySpreadIdr());
    }

    @Test
    void getResourceType_returnsCorrectType() {
        assertEquals("latest_idr_rates", fetcher.getResourceType());
    }
}
