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
import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CurrenciesFetcherTest {

    private MockWebServer mockWebServer;
    private CurrenciesFetcher fetcher;

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

        fetcher = new CurrenciesFetcher(factoryBean, "/currencies");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchData_shouldReturnCurrenciesMap() {
        String jsonResponse = """
                {
                    "USD": "US Dollar",
                    "EUR": "Euro",
                    "IDR": "Indonesian Rupiah"
                }
                """;
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));

        DataResult result = fetcher.fetchData();

        assertThat(result.getResourceType()).isEqualTo("supported_currencies");
        Map<String, String> data = (Map<String, String>) result.getData();
        assertThat(data).containsEntry("USD", "US Dollar");
        assertThat(data).containsEntry("EUR", "Euro");
        assertThat(data).containsEntry("IDR", "Indonesian Rupiah");
    }
}