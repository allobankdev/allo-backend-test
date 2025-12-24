package com.allo.finance.strategy.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedCurrenciesFetcherTest {

    @Test
    void shouldReturnCurrencyList() {
        WebClient webClient = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Map<String, String> mockResponse = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        Mockito.when(
                webClient.get()
                        .uri("/currencies")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(mockResponse);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(webClient);

        Object result = fetcher.fetchData();

        Map<String, String> resultMap = (Map<String, String>) result;

        assertThat(resultMap).containsKey("USD");
        assertThat(resultMap.get("IDR")).isEqualTo("Indonesian Rupiah");
    }

}