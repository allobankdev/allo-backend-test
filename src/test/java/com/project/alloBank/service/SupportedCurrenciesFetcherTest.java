package com.project.alloBank.service;

import com.project.alloBank.dto.CurrencyMapResponse;
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
public class SupportedCurrenciesFetcherTest {
    @Autowired
    private SupportedCurrenciesFetcher fetcher;

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
        stubFor(get(urlEqualTo("/currencies"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                          "USD": "United States Dollar",
                          "IDR": "Indonesian Rupiah",
                          "EUR": "Euro"
                        }
                        """)));
    }

    @Test
    void testFetchData() {
        Object result = fetcher.fetchData();
        assertNotNull(result);
        assertTrue(result instanceof CurrencyMapResponse);

        CurrencyMapResponse response = (CurrencyMapResponse) result;

        assertEquals(3, response.getCurrencies().size());
        assertEquals("United States Dollar", response.getCurrencies().get("USD"));
        assertEquals("Indonesian Rupiah", response.getCurrencies().get("IDR"));
        assertTrue(response.getCurrencies().containsKey("EUR"));
    }
}
