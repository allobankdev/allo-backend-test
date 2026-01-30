package com.example.demo.strategy;

import com.example.demo.dto.LatestResponse;
import com.example.demo.dto.LatestResult;
import com.example.demo.util.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LatestRatesFetcherTest {

    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    private LatestRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        fetcher = new LatestRatesFetcher(webClient);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void shouldFetchLatestRatesAndCalculateSpread() {

        LatestResponse fakeResponse = new LatestResponse();
        fakeResponse.setBase("IDR");
        fakeResponse.setDate("2024-01-01");
        fakeResponse.setRates(Map.of("USD", 0.00006));

        when(responseSpec.bodyToMono(LatestResponse.class))
                .thenReturn(Mono.just(fakeResponse));

        List<?> result = fetcher.fetchData();

        assertEquals(1, result.size());

        LatestResult latestResult = (LatestResult) result.get(0);

        double spreadFactor = SpreadCalculator.calculate("raihan2-byte");
        double expected = (1 / 0.00006) * (1 + spreadFactor);

        assertEquals(expected, latestResult.getUsdBuySpreadIdr(), 0.01);
        assertEquals("IDR", latestResult.getOriginalData().getBase());
    }

}
