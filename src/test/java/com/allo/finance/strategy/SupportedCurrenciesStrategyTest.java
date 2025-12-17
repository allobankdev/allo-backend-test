package com.allo.finance.strategy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesStrategyTest {

    @Test
    void shouldFetchSupportedCurrencies() {
        WebClient client = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(
                client.get()
                        .uri("/currencies")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(Map.of("USD", "US Dollar"));

        SupportedCurrenciesStrategy strategy = new SupportedCurrenciesStrategy(client);

        Object result = strategy.fetch();

        assertNotNull(result);
    }
}
