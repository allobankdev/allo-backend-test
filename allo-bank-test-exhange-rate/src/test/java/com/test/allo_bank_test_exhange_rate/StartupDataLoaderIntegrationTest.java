package com.test.allo_bank_test_exhange_rate;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StartupDataLoaderIntegrationTest {
    
    private static WireMockServer wireMockServer;

    @Autowired
    WebTestClient webTestClient;

    static void configureWireMockStub(DynamicPropertyRegistry registry) {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock")
        );
        wireMockServer.start();
        registry.add("app.frankfurter.base-url", () -> wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        Optional.ofNullable(wireMockServer)
        .filter(WireMockServer::isRunning)
        .ifPresent(WireMockServer::stop);
    }
    

    @Test
    void testApplicationRunner() {
        Map<String, Object> responseLatestIdrRates = webTestClient.get()
            .uri("/api/finance/latest_idr_rates")
            .exchange()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();
        
        Map<String, Object> responseHistoricalIdrUsd = webTestClient.get()
            .uri("/api/finance/historical_idr_usd")
            .exchange()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

        Map<String, Object> responseSupportedCurrencies = webTestClient.get()
            .uri("/api/finance/supported_currencies")
            .exchange()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();
        
        assertThat(responseLatestIdrRates).isNotNull();
        assertThat(responseHistoricalIdrUsd).isNotNull();
        assertThat(responseSupportedCurrencies).isNotNull();

        assertEquals(true, responseLatestIdrRates.containsKey("computed"));
        assertEquals(true, responseHistoricalIdrUsd.containsKey("rates"));
        assertEquals("United States Dollar", responseSupportedCurrencies.get("USD"));
    }
}
