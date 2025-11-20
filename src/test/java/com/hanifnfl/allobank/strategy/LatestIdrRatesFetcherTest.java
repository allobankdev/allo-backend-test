package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.LatestIdrRatesView;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LatestIdrRatesFetcherTest {

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldLoadLatestRatesAndCalculateUsdBuySpreadIdr() {
        // given: mock Frankfurter /latest?base=IDR
        String json = """
            {
              "amount": 1.0,
              "base": "IDR",
              "date": "2024-02-09",
              "rates": {
                "USD": 0.000064
              }
            }
            """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        String baseUrl = mockWebServer.url("/").toString();

        WebClient client = WebClient.builder()
                .baseUrl(baseUrl.substring(0, baseUrl.length() - 1)) // remove trailing /
                .build();

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher("hanifnfl097");

        // when
        fetcher.loadData(client);
        List<LatestIdrRatesView> cached = fetcher.getCachedData();

        // then
        assertThat(cached).hasSize(1);
        LatestIdrRatesView view = cached.get(0);

        assertThat(view.base()).isEqualTo("IDR");
        assertThat(view.spreadFactor()).isEqualByComparingTo(new BigDecimal("0.00998"));

        // Manual expected value:
        // Rate_USD = 0.000064
        // inverted = 1 / 0.000064 = 15625
        // usdBuySpreadIdr = 15625 * (1 + 0.00998) = 15625 * 1.00998 ≈ 15780.9375
        BigDecimal expected = new BigDecimal("15780.9375");
        assertThat(view.usdBuySpreadIdr())
                .isEqualByComparingTo(expected.setScale(4)); // sesuai dengan scale di implementation
    }
}
