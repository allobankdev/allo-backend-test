package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.CurrencyView;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedCurrenciesFetcherTest {

    private MockWebServer mockWebServer;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        fetcher = new SupportedCurrenciesFetcher();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void loadData_shouldTransformFrankfurterMap_toCurrencyViewList() {
        String body = """
            {
              "USD": "United States Dollar",
              "EUR": "Euro",
              "IDR": "Indonesian Rupiah"
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
        List<CurrencyView> cached = fetcher.getCachedData();
        assertThat(cached).hasSize(3);

        assertThat(cached)
                .extracting(CurrencyView::symbol)
                .containsExactlyInAnyOrder("USD", "EUR", "IDR");

        assertThat(cached)
                .extracting(CurrencyView::description)
                .contains(
                        "United States Dollar",
                        "Euro",
                        "Indonesian Rupiah"
                );
    }
}
