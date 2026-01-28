package com.example.demo.strategy;

import com.example.demo.dto.HistoricalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HistoricalRatesFetcherTest {

    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    private HistoricalRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        fetcher = new HistoricalRatesFetcher(webClient);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void shouldFetchHistoricalRates() {

        HistoricalResponse fakeResponse = new HistoricalResponse();
        fakeResponse.setBase("IDR");
        fakeResponse.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.00006)
        ));

        when(responseSpec.bodyToMono(HistoricalResponse.class))
                .thenReturn(Mono.just(fakeResponse));

        List<?> result = fetcher.fetchData();

        assertEquals(1, result.size());
    }

}
