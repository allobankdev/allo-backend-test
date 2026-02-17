package com.exchange.frankfurter.integration;

import com.exchange.frankfurter.store.FinanceDataStore;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Mengambil config dari application-test.yml
@AutoConfigureWebTestClient
class FinanceIntegrationTest {

    private static MockWebServer mockBackEnd;

    @Autowired
    private FinanceDataStore dataStore;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        // Kita kunci di port 8081 sesuai application-test.yml
        mockBackEnd.start(8081);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Should preload data on startup and serve it via controller")
    void verifyFullFlow() throws Exception {
        // 1. PREPARE: Siapkan respon JSON untuk 3 Fetcher yang jalan di startup
        // (Urutan enqueue harus sama dengan urutan panggil di ApplicationRunner)

        String latestJson = "{\"base\":\"IDR\",\"rates\":{\"USD\":0.000064}}";
        String historyJson = "{\"rates\":{\"2024-01-01\":{\"USD\":0.000065}}}";
        String currenciesJson = "{\"USD\":\"United States Dollar\"}";

        mockBackEnd.enqueue(new MockResponse().setBody(latestJson).addHeader("Content-Type", "application/json"));
        mockBackEnd.enqueue(new MockResponse().setBody(historyJson).addHeader("Content-Type", "application/json"));
        mockBackEnd.enqueue(new MockResponse().setBody(currenciesJson).addHeader("Content-Type", "application/json"));

        // 2. VERIFY STORE: Cek apakah data sudah masuk ke FinanceDataStore
        // Kita beri sedikit jeda jika proses fetch di Runner bersifat async, 
        // tapi jika sinkron, data harusnya sudah ada.
        assertThat(dataStore.get("latest_idr_rates")).isNotNull();

        // 3. VERIFY CONTROLLER: Tembak endpoint asli
        webTestClient.get()
                .uri("/api/finance/data/latest_idr_rates")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.resourceType").isEqualTo("latest_idr_rates")
                // Verifikasi kalkulasi spread: (1 / 0.000064) * (1 + spread)
                .jsonPath("$.data.USD_BuySpread_IDR").exists();
    }
}