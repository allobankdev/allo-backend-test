package com.project.alloBank.service;

import com.project.alloBank.dto.HistoricalRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
public class HistoricalIdrUsdFetcherTest {

    @Autowired
    private HistoricalIdrUsdFetcher fetcher;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WebClient webClient() {
            return WebClient.builder()
                    .baseUrl("http://localhost:8089")
                    .build();
        }
    }

    @BeforeEach
    void setup() {
        stubFor(get(urlEqualTo("/2024-01-01..2024-01-05?from=IDR&to=USD"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "amount": 1,
                              "base": "IDR",
                              "start_date": "2024-01-01",
                              "end_date": "2024-01-05",
                              "rates": {
                                  "2024-01-01": {"USD": 0.000064},
                                  "2024-01-02": {"USD": 0.000065}
                              }
                            }
                        """)));
    }

    @Test
    void testFetchData() {
        HistoricalRatesResponse result = (HistoricalRatesResponse) fetcher.fetchData();

        assertNotNull(result);
        assertNotNull(result.getData());

        assertEquals("IDR", result.getData().get("base"));
        assertTrue(result.getData().containsKey("rates"));
    }
}
