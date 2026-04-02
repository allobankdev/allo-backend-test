package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import com.allobank.frankfurter.model.DataResult;
import com.allobank.frankfurter.model.HistoricalRatesResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalRatesFetcherTest {

    private MockWebServer mockWebServer;
    private HistoricalRatesFetcher fetcher;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        WebClientFactoryBean factoryBean = new WebClientFactoryBean(
                mockWebServer.url("/").toString(),
                Duration.ofMillis(5000),
                Duration.ofMillis(5000)) {
            @Override
            public WebClient getObject() {
                return webClient;
            }
        };

        fetcher = new HistoricalRatesFetcher(factoryBean, "/%s..%s?from=IDR&to=USD", "2024-01-01", "2024-01-05");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldReturnHistoricalRates() {
        String jsonResponse = """
                {
                    "rates": {
                        "2024-01-01": {"USD": 0.000064},
                        "2024-01-02": {"USD": 0.000063},
                        "2024-01-03": {"USD": 0.000065}
                    },
                    "start_date": "2024-01-01",
                    "end_date": "2024-01-05",
                    "base": "IDR"
                }
                """;
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));

        DataResult result = fetcher.fetchData();

        assertThat(result.getResourceType()).isEqualTo("historical_idr_usd");
        HistoricalRatesResponse data = (HistoricalRatesResponse) result.getData();
        assertThat(data.getRates()).isNotNull();
        assertThat(data.getStartDate()).isEqualTo("2024-01-01");
        assertThat(data.getEndDate()).isEqualTo("2024-01-05");
        assertThat(data.getBase()).isEqualTo("IDR");
    }
}