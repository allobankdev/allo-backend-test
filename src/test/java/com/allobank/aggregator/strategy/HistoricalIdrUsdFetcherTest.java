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

class HistoricalIdrUsdFetcherTest {

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
        String body = "{\n" +
                "  \"rates\": {\n" +
                "    \"2024-01-01\": { \"USD\": 0.000065 },\n" +
                "    \"2024-01-02\": { \"USD\": 0.000064 }\n" +
                "  }\n" +
                "}";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body).addHeader("Content-Type","application/json"));

        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client);
        FinanceDataDto dto = fetcher.fetch();

        assertThat(dto.resourceType()).isEqualTo("historical_idr_usd");
        Map<?,?> payload = dto.payload();
        assertThat(payload.get("range")).isEqualTo("2024-01-01..2024-01-05");
        assertThat(payload.get("rates")).isInstanceOf(Map.class);
    }

    @Test
    void fetch_error_throws() {
        server.enqueue(new MockResponse().setResponseCode(500));
        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client);
        assertThatThrownBy(fetcher::fetch).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Frankfurter returned error");
    }
}
