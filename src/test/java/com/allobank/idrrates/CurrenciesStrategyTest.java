package com.allobank.idrrates;

import com.allobank.idrrates.dto.CurrenciesDTO;
import com.allobank.idrrates.dto.TimeseriesRatesDTO;
import com.allobank.idrrates.strategy.CurrenciesStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CurrenciesStrategyTest {

    private MockWebServer mockWebServer;
    private CurrenciesStrategy strategy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        strategy = new CurrenciesStrategy();
        strategy.webClient = webClient;
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getResourceType_shouldReturnSupportedCurrencies() {
        assertThat(strategy.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    void fetchData_shouldReturnCurrenciesMap() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "AUD": "Australian Dollar",
                            "USD": "United States Dollar",
                            "IDR": "Indonesian Rupiah"
                        }
                        """)
                .addHeader("Content-Type", "application/json"));

        CurrenciesDTO result = (CurrenciesDTO) strategy.fetchData();

        assertThat(result).isNotNull();
        assertThat(result.getCurrencies()).containsKey("AUD");
        assertThat(result.getCurrencies()).containsKey("USD");
        assertThat(result.getCurrencies()).containsKey("IDR");
        assertThat(result.getCurrencies().get("IDR")).isEqualTo("Indonesian Rupiah");
    }
}