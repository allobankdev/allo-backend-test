package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import com.allobank.frankfurter.model.DataResult;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LatestRatesFetcherTest {

    private MockWebServer mockWebServer;
    private WebClientFactoryBean factoryBean;
    private LatestRatesFetcher fetcher;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        // Create a mock factory bean that returns the mock WebClient
        factoryBean = new WebClientFactoryBean(mockWebServer.url("/").toString(), Duration.ofMillis(5000), Duration.ofMillis(5000)) {
            @Override
            public WebClient getObject() {
                return webClient;
            }
        };

        fetcher = new LatestRatesFetcher(factoryBean, "testuser", "/latest?base=IDR");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldCalculateSpreadCorrectly() {
        String jsonResponse = """
                {
                    "base": "IDR",
                    "date": "2024-01-01",
                    "rates": {"USD": 0.000064}
                }
                """;
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));

        DataResult result = fetcher.fetchData();

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertThat(data.get("base")).isEqualTo("IDR");
        assertThat(data.get("USD_BuySpread_IDR")).isEqualTo(new BigDecimal("15764.84"));
    }
}