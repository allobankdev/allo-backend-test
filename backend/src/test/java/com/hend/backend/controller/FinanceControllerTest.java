package com.hend.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author : hend wunga
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class FinanceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetLatestIdrRates_Success() {
        webTestClient.get()
                .uri("/api/finance/data/latest_idr_rates")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.usdBuySpreadIdr").exists()
                .jsonPath("$.spreadFactor").isEqualTo(0.00097);
    }

    @Test
    void testGetSupportedCurrencies_Success() {
        webTestClient.get()
                .uri("/api/finance/data/supported_currencies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.USD").isEqualTo("United States Dollar");
    }

    @Test
    void testGetInvalidResource_Returns404() {
        webTestClient.get()
                .uri("/api/finance/data/invalid_resource")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").exists();
    }
}
