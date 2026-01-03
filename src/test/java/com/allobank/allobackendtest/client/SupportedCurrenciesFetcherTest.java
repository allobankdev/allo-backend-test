package com.allobank.allobackendtest.client;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.allobackendtest.strategy.SupportedCurrenciesFetcher;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@SpringBootTest
class SupportedCurrenciesFetcherTest { 
    private MockWebServer mockWebServer;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        fetcher = new SupportedCurrenciesFetcher(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldFetchSupportedCurrenciesSuccessfully() throws Exception {
        // given: response asli Frankfurter
        String responseBody = """
        {
            "AUD": "Australian Dollar",
            "BRL": "Brazilian Real",
            "CAD": "Canadian Dollar",
            "CHF": "Swiss Franc",
            "IDR": "Indonesian Rupiah",
            "USD": "United States Dollar"
        }
        """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                        .setBody(responseBody)
        );

        // when
        Map<String, String> result = (Map<String, String>) fetcher.fetchData();

        // then
        assertThat(result).isNotNull();
        assertThat(result).containsEntry("IDR", "Indonesian Rupiah");
        assertThat(result).containsEntry("USD", "United States Dollar");

        // verify request
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/currencies");
    }

}
