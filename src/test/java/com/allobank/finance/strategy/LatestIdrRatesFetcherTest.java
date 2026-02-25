package com.allobank.finance.strategy;

import com.allobank.finance.config.FrankfurterProperties;
import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.model.LatestRateResponse;
import com.allobank.finance.strategy.impl.LatestIdrRatesFetcher;
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
import static org.assertj.core.api.Assertions.within;

/**
 * Unit test untuk {@link LatestIdrRatesFetcher}.
 * Menggunakan MockWebServer untuk menstub HTTP call ke Frankfurter API.
 */
class LatestIdrRatesFetcherTest {

    private MockWebServer mockWebServer;
    private WebClient webClient;
    private LatestIdrRatesFetcher fetcher;

    // GitHub username: ramandry12
    // Unicode sum: r=114,a=97,m=109,a=97,n=110,d=100,r=114,y=121,1=49,2=50
    // Sum = 114+97+109+97+110+100+114+121+49+50 = 961
    // Spread Factor = (961 % 1000) / 100000.0 = 961 / 100000.0 = 0.00961
    private static final double EXPECTED_SPREAD_FACTOR = 0.00961;
    private static final String GITHUB_USERNAME = "ramandry12";

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setGithubUsername(GITHUB_USERNAME);
        fetcher = new LatestIdrRatesFetcher(properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Harus mengembalikan resource type yang benar")
    void shouldReturnCorrectResourceType() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    @DisplayName("Harus menghitung spread factor dengan benar untuk username 'ramandry12'")
    void shouldCalculateSpreadFactorCorrectly() {
        double actual = LatestIdrRatesFetcher.calculateSpreadFactor(GITHUB_USERNAME);
        assertThat(actual).isEqualTo(EXPECTED_SPREAD_FACTOR, within(0.000001));
    }

    @Test
    @DisplayName("Harus menghitung USD_BuySpread_IDR dengan benar")
    void shouldCalculateUsdBuySpreadIdr() {
        // Given: respons mock dari API
        // Rate USD = 0.000064 (artinya 1 IDR = 0.000064 USD)
        // 1/0.000064 = 15625.0
        // 15625.0 * (1 + 0.00961) = 15625.0 * 1.00961 = 15775.15625
        String mockBody = """
                {
                    "amount": 1.0,
                    "base": "IDR",
                    "date": "2024-01-05",
                    "rates": {
                        "USD": 0.000064,
                        "EUR": 0.000059
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
        assertThat(result.resourceType()).isEqualTo("latest_idr_rates");

        LatestRateResponse response = (LatestRateResponse) result.data();
        assertThat(response).isNotNull();
        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates()).containsKey("USD");

        // Verifikasi kalkulasi: (1 / 0.000064) * (1 + 0.00576) = 15715.0
        double expectedUsdBuySpreadIdr = (1.0 / 0.000064) * (1.0 + EXPECTED_SPREAD_FACTOR);
        assertThat(response.getUsdBuySpreadIdr())
                .isEqualTo(expectedUsdBuySpreadIdr, within(0.01));
    }

    @Test
    @DisplayName("Harus melempar ExternalApiException ketika API mengembalikan 500")
    void shouldThrowExternalApiExceptionOnServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

        assertThatThrownBy(() -> fetcher.fetch(webClient))
                .isInstanceOf(ExternalApiException.class);
    }

    @Test
    @DisplayName("Harus melempar ExternalApiException ketika API mengembalikan 404")
    void shouldThrowExternalApiExceptionOnClientError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody("Not Found"));

        assertThatThrownBy(() -> fetcher.fetch(webClient))
                .isInstanceOf(ExternalApiException.class);
    }

    @Test
    @DisplayName("calculateSpreadFactor harus melempar exception untuk username kosong")
    void shouldThrowForBlankUsername() {
        assertThatThrownBy(() -> LatestIdrRatesFetcher.calculateSpreadFactor(""))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> LatestIdrRatesFetcher.calculateSpreadFactor(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
