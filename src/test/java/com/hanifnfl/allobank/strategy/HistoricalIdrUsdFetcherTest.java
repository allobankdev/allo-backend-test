package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.HistoricalIdrUsdView;
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

class HistoricalIdrUsdFetcherTest {

    private MockWebServer mockWebServer;
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        fetcher = new HistoricalIdrUsdFetcher();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void loadData_shouldTransformFrankfurterTimeseries_toViewList() {
        String body = """
            {
              "base": "IDR",
              "start_date": "2024-01-01",
              "end_date": "2024-01-05",
              "rates": {
                "2024-01-01": { "USD": 0.000064 },
                "2024-01-02": { "USD": 0.000065 }
              }
            }
            """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        WebClient client = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        // act
        fetcher.loadData(client);

        // assert
        List<HistoricalIdrUsdView> cached = fetcher.getCachedData();
        assertThat(cached).hasSize(2);

        assertThat(cached)
                .extracting(HistoricalIdrUsdView::date)
                .containsExactlyInAnyOrder("2024-01-01", "2024-01-02");

        assertThat(cached)
                .extracting(HistoricalIdrUsdView::usdRate)
                .containsExactlyInAnyOrder(
                        new BigDecimal("0.000064"),
                        new BigDecimal("0.000065")
                );
    }
}
