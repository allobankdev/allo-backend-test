package com.allobank.aggregator.controller;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.service.FinanceDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

class FinanceControllerTest {

    private FinanceDataStore store;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        store = Mockito.mock(FinanceDataStore.class);
        FinanceController controller = new FinanceController(store);
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void getByResourceType_whenExists_returns200() {
        FinanceDataDto dto = new FinanceDataDto("supported_currencies", Map.of("k","v"));
        when(store.get("supported_currencies")).thenReturn(Optional.of(dto));
        when(store.all()).thenReturn(Map.of("supported_currencies", dto));

        client.get()
                .uri("/api/finance/data/supported_currencies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.resourceType").isEqualTo("supported_currencies")
                .jsonPath("$.payload.k").isEqualTo("v");
    }

    @Test
    void getByResourceType_whenMissing_returns400WithAllowed() {
        when(store.get("missing")).thenReturn(Optional.empty());
        when(store.all()).thenReturn(Map.of("supported_currencies", new FinanceDataDto("supported_currencies", Map.of())));

        client.get()
                .uri("/api/finance/data/missing")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(body -> {
                    assert body.contains("Invalid resourceType");
                    assert body.contains("supported_currencies");
                });
    }
}
