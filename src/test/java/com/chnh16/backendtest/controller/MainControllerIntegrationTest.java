package com.chnh16.backendtest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("latest_idr_rates")
    public void latestIdrRates() {
        ResponseEntity<Object> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/finance/data/latest_idr_rates", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("historical_idr_usd")
    public void historicalIdrUsd() {
        ResponseEntity<Object> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/finance/data/historical_idr_usd", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("supported_currencies")
    public void supportedCurrencies() {
        ResponseEntity<Object> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/finance/data/supported_currencies", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("method_not_implemented")
    public void methodNotImplemented() {
        ResponseEntity<Object> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/finance/data/unimplemented_method", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        assertNotNull(response.getBody());
    }

}
