package com.allo.finance.strategy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesStrategyTest {

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        WebClient client = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(
                client.get()
                        .uri("/latest?base=IDR")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(
                Map.of("rates", Map.of("USD", 0.000065))
        );

        LatestIdrRatesStrategy strategy = new LatestIdrRatesStrategy(client);

        Map<String, Object> result = (Map<String, Object>) strategy.fetch();

        assertTrue(result.containsKey("USD_BuySpread_IDR"));
        assertNotNull(result.get("USD_BuySpread_IDR"));
    }
}
