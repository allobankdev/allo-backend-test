package com.example.allow.test;

import com.example.allow.model.LatestRatesResponse;
import com.example.allow.model.UsdBuySpreadWrapper;
import com.example.allow.strategy.LatestIdrRatesFetcher;
import com.example.allow.util.SpreadCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class LatestIdrRatesFetcherTest {

    @Test
    void shouldCalculateSpreadCorrectly() {
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);

        when(uriSpec.uri((Function) any(Function.class))).thenReturn(headersSpec);

        when(headersSpec.retrieve()).thenReturn(responseSpec);

        LatestRatesResponse mockResponse = new LatestRatesResponse(
                "IDR",
                "2025-11-20",
                Map.of("USD", 0.000060)
        );
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        SpreadCalculator calc = mock(SpreadCalculator.class);
        when(calc.getSpreadFactor()).thenReturn(0.00849);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(webClient, calc);


        Object result = fetcher.fetchData().block();
        assertThat(result).isInstanceOf(UsdBuySpreadWrapper.class);

        UsdBuySpreadWrapper wrapper = (UsdBuySpreadWrapper) result;
        assertThat(wrapper.USD_BuySpread_IDR()).isCloseTo(16808.16, within(1.0));
        assertThat(wrapper.appliedSpreadFactor()).isEqualTo(0.00849);
        assertThat(wrapper.githubUsername()).isEqualTo("hafizs08");
    }
}
