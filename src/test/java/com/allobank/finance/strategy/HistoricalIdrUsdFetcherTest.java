package com.allobank.finance.strategy;

import com.allobank.finance.config.FrankfurterProperties;
import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.model.HistoricalRateResponse;
import com.allobank.finance.strategy.impl.HistoricalIdrUsdFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit test untuk {@link HistoricalIdrUsdFetcher}.
 */
class HistoricalIdrUsdFetcherTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setHistoricalStart("2024-01-01");
        properties.setHistoricalEnd("2024-01-05");
        fetcher = new HistoricalIdrUsdFetcher(properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Harus mengembalikan resource type yang benar")
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    @DisplayName("Harus mengembalikan data historis IDR→USD dengan benar")
    void shouldFetchHistoricalDataSuccessfully() {
        // Given: mock response historical rates
        String mockBody = """
                {
                    "amount": 1.0,
                    "base": "IDR",
                    "start_date": "2024-01-01",
                    "end_date": "2024-01-05",
                    "rates": {
                        "2024-01-02": {"USD": 0.000064},
                        "2024-01-03": {"USD": 0.000064},
                        "2024-01-04": {"USD": 0.000065},
                        "2024-01-05": {"USD": 0.000064}
                    }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // When
        List<FinanceDataResult> results = fetcher.fetch(webClient);

        // Then
        assertThat(results).hasSize(1);

        FinanceDataResult result = results.get(0);
        assertThat(result.resourceType()).isEqualTo("historical_idr_usd");

        HistoricalRateResponse response = (HistoricalRateResponse) result.data();
        assertThat(response).isNotNull();
        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates()).hasSize(4);
        assertThat(response.getRates()).containsKey("2024-01-02");
        assertThat(response.getRates().get("2024-01-02")).containsEntry("USD", 0.000064);
    }

    @Test
    @DisplayName("Harus melempar ExternalApiException ketika API gagal (5xx)")
    void shouldThrowExternalApiExceptionOnServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        assertThatThrownBy(() -> fetcher.fetch(webClient))
                .isInstanceOf(ExternalApiException.class);
    }
}
