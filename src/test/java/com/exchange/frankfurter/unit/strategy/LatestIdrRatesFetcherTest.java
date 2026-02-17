package com.exchange.frankfurter.unit.strategy;

import com.exchange.frankfurter.service.SpreadCalculator;
import com.exchange.frankfurter.strategy.LatestIdrRatesFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LatestIdrRatesFetcherTest {

    private static MockWebServer mockWebServer;
    private LatestIdrRatesFetcher fetcher;
    private SpreadCalculator spreadCalculator;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        // Build WebClient yang mengarah ke MockWebServer lokal
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        spreadCalculator = Mockito.mock(SpreadCalculator.class);
        fetcher = new LatestIdrRatesFetcher(webClient, spreadCalculator);
    }

    @Test
    @DisplayName("Should calculate USD Buy Spread correctly from API response")
    void testFetchDataAndTransform() {
        // 1. Persiapkan Mock API Response
        // Frankfurter mengembalikan rates: { "USD": 0.000064 } jika base IDR
        String mockBody = "{\"base\":\"IDR\",\"rates\":{\"USD\":0.000064}}";
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBody)
                .addHeader("Content-Type", "application/json"));

        // 2. Mock Spread Calculator (misal return 0.0036 dari 'xxx')
        when(spreadCalculator.calculateSpreadFactor()).thenReturn(0.0036);

        // 3. Jalankan Fetcher
        Map<String, Object> result = (Map<String, Object>) fetcher.fetchData();

        // 4. Verifikasi Rumus: (1 / 0.000064) * (1 + 0.0036)
        // 1 / 0.000064 = 15625
        // 15625 * 1.0036 = 15681.25
        double expectedSpread = 15681.25;

        assertThat(result).containsKey("USD_BuySpread_IDR");
        assertThat((Double) result.get("USD_BuySpread_IDR")).isEqualTo(expectedSpread);
    }
}
