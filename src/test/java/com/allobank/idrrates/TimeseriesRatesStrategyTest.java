package com.allobank.idrrates;

import com.allobank.idrrates.dto.LatestRatesDTO;
import com.allobank.idrrates.strategy.LatestRatesStrategy;
import com.allobank.idrrates.strategy.TimeseriesRatesStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.idrrates.dto.TimeseriesRatesDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TimeseriesRatesStrategyTest {

    private MockWebServer mockWebServer;
    private TimeseriesRatesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new TimeseriesRatesStrategy();
        strategy.webClient = webClient;
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getResourceType_shouldReturnHistoricalIdrUsd() {
        assertThat(strategy.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    void fetchData_shouldReturnTimeseriesRatesWithDates() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "amount": 1.0,
                            "base": "IDR",
                            "start_date": "2025-12-31",
                            "end_date": "2026-01-30",
                            "rates": {
                                "2025-12-31": { "USD": 0.000060 },
                                "2026-01-02": { "USD": 0.000060 }
                            }
                        }
                        """)
                .addHeader("Content-Type", "application/json"));

        TimeseriesRatesDTO result = (TimeseriesRatesDTO) strategy.fetchData();

        assertThat(result).isNotNull();
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getStartDate()).isEqualTo("2025-12-31");
        assertThat(result.getEndDate()).isEqualTo("2026-01-30");
        assertThat(result.getRates()).containsKey("2025-12-31");
        assertThat(result.getRates()).containsKey("2026-01-02");
    }
}