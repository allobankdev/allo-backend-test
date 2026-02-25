package com.allobank.finance.strategy;

import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.strategy.impl.SupportedCurrenciesFetcher;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit test untuk {@link SupportedCurrenciesFetcher}.
 */
class SupportedCurrenciesFetcherTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        fetcher = new SupportedCurrenciesFetcher();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Harus mengembalikan resource type yang benar")
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    @DisplayName("Harus mengembalikan daftar mata uang yang benar")
    void shouldFetchSupportedCurrenciesSuccessfully() {
        // Given
        String mockBody = """
                {
                    "AUD": "Australian Dollar",
                    "EUR": "Euro",
                    "IDR": "Indonesian Rupiah",
                    "USD": "US Dollar"
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
        assertThat(result.resourceType()).isEqualTo("supported_currencies");

        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result.data();
        assertThat(currencies).hasSize(4);
        assertThat(currencies).containsEntry("IDR", "Indonesian Rupiah");
        assertThat(currencies).containsEntry("USD", "US Dollar");
        assertThat(currencies).containsEntry("EUR", "Euro");
    }

    @Test
    @DisplayName("Harus melempar ExternalApiException ketika API mengembalikan error")
    void shouldThrowExternalApiExceptionOnError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody("Service Unavailable"));

        assertThatThrownBy(() -> fetcher.fetch(webClient))
                .isInstanceOf(ExternalApiException.class);
    }
}
