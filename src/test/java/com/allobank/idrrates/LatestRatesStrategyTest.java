package com.allobank.idrrates;

import com.allobank.idrrates.dto.LatestRatesDTO;
import com.allobank.idrrates.strategy.LatestRatesStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LatestRatesStrategyTest {

    private MockWebServer mockWebServer;
    private LatestRatesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new LatestRatesStrategy();
        strategy.webClient = webClient;
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getResourceType_shouldReturnLatestIdrRates() {
        assertThat(strategy.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    void fetchData_shouldReturnLatestRatesWithSpread() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "amount": 1.0,
                            "base": "IDR",
                            "date": "2026-03-30",
                            "rates": { "USD": 0.000059 }
                        }
                        """)
                .addHeader("Content-Type", "application/json"));

        LatestRatesDTO result = (LatestRatesDTO) strategy.fetchData();

        assertThat(result).isNotNull();
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getRates()).containsKey("USD");
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isGreaterThan(0);
    }
}