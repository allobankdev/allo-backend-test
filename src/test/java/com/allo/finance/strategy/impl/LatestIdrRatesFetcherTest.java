package com.allo.finance.strategy.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

class LatestIdrRatesFetcherTest {

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        WebClient webClient = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Map<String, Object> mockResponse = Map.of(
                "rates", Map.of("USD", 0.00006)
        );

        Mockito.when(
                webClient.get()
                        .uri(anyString())
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(mockResponse);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(webClient);

        Object result = fetcher.fetchData();

        Map<String, Object> resultMap = (Map<String, Object>) result;

        assertThat(resultMap).containsKey("USD_BuySpread_IDR");

        double spread = (double) resultMap.get("USD_BuySpread_IDR");
        assertThat(spread).isGreaterThan(0);
    }

}