package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.util.SpreadCalculator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LatestIdrRatesFetcherTest {

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
    void fetch_success_enrichesUsdWithSpread() {
        String body = "{\n" +
                "  \"base\": \"IDR\",\n" +
                "  \"date\": \"2024-01-02\",\n" +
                "  \"rates\": {\n" +
                "    \"USD\": 0.000064,\n" +
                "    \"EUR\": 0.000059\n" +
                "  }\n" +
                "}";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body).addHeader("Content-Type","application/json"));

        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        String username = "abc"; // deterministic factor 0.00294
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, username);

        FinanceDataDto dto = fetcher.fetch();
        assertThat(dto.resourceType()).isEqualTo("latest_idr_rates");

        Map<?,?> payload = dto.payload();
        assertThat(payload.get("base")).isEqualTo("IDR");
        assertThat(payload.get("date")).isEqualTo("2024-01-02");

        Map<?,?> rates = (Map<?,?>) payload.get("rates");
        Map<?,?> usdDetail = (Map<?,?>) rates.get("USD");
        assertThat(usdDetail).isNotNull();
        assertThat(usdDetail.get("rate")).isInstanceOf(BigDecimal.class);
        assertThat(usdDetail.get("spreadFactor")).isEqualTo(String.format("%.5f", SpreadCalculator.computeSpreadFactor(username)));

        // Ensure non-USD still present
        Map<?,?> eurDetail = (Map<?,?>) rates.get("EUR");
        assertThat(eurDetail.get("rate")).isInstanceOf(BigDecimal.class);
    }

    @Test
    void fetch_error_throws() {
        server.enqueue(new MockResponse().setResponseCode(500));
        WebClient client = WebClient.builder().baseUrl(server.url("/").toString()).build();
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, "user");

        assertThatThrownBy(fetcher::fetch).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Frankfurter returned error");
    }
}
