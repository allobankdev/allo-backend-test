package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class SupportedCurrenciesFetcherTest {

    private MockWebServer server;

    @BeforeEach
    void setup() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void fetch_success() {
        String body = "{\n  \"IDR\": \"Indonesian Rupiah\",\n  \"USD\": \"US Dollar\"\n}";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body).addHeader("Content-Type","application/json"));

        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);
        FinanceDataDto dto = fetcher.fetch();

        assertThat(dto.resourceType()).isEqualTo("supported_currencies");
        Map<?,?> payload = dto.payload();
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) payload.get("currencies");
        assertThat(currencies.get("IDR")).isEqualTo("Indonesian Rupiah");
        assertThat(currencies.get("USD")).isEqualTo("US Dollar");
    }

    @Test
    void fetch_error_throws() {
        server.enqueue(new MockResponse().setResponseCode(500));
        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);
        assertThatThrownBy(fetcher::fetch).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Frankfurter returned error");
    }
}
