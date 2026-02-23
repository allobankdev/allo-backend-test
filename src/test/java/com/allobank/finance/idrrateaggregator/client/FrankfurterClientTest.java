package com.allobank.finance.idrrateaggregator.client;


import com.allobank.finance.idrrateaggregator.model.dto.HistoricalRatesResponse;
import com.allobank.finance.idrrateaggregator.model.dto.LatestRatesResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FrankfurterClientTest {
    private static MockWebServer mockWebServer;
    private FrankfurterClient client;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setupClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        client = new FrankfurterClient(webClient);
    }

    @Test
    void shouldFetchLatestIdrRates() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                    {
                      "amount": 1.0,
                      "base": "IDR",
                      "date": "2026-02-03",
                      "rates": {
                        "USD": 0.00006
                      }
                    }
                    """)
                .addHeader("Content-Type", "application/json"));

        LatestRatesResponse response = client.getLatestIdrRates();

        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates().get("USD")).isEqualTo(0.00006);
    }

    @Test
    void shouldFetchHistoricalIdrUsdRates() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                    {
                      "amount": 1.0,
                      "base": "IDR",
                      "start_date": "2023-12-29",
                      "end_date": "2024-01-05",
                      "rates": {
                        "2023-12-29": { "USD": 0.000065 }
                      }
                    }
                    """)
                .addHeader("Content-Type", "application/json"));

        HistoricalRatesResponse response = client.getHistoricalIdrUsdRates();

        assertThat(response.getRates())
                .containsKey("2023-12-29");
    }

    @Test
    void shouldFetchSupportedCurrencies() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                    {
                      "USD": "United States Dollar",
                      "IDR": "Indonesian Rupiah"
                    }
                    """)
                .addHeader("Content-Type", "application/json"));

        Map<String, String> currencies = client.getSupportedCurrencies();

        assertThat(currencies)
                .containsEntry("USD", "United States Dollar")
                .containsEntry("IDR", "Indonesian Rupiah");
    }
}
