package com.musfiul.idrrateaggregator.client;

import com.musfiul.idrrateaggregator.dto.CurrenciesResponse;
import com.musfiul.idrrateaggregator.dto.HistoricalRatesResponse;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FrankfurterClientImplTest {

    @Mock
    private WebClient.Builder builder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private FrankfurterClientImpl client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        client = new FrankfurterClientImpl(builder, "https://fake-url.com");
    }

    @Test
    void shouldReturnLatestRates() {

        LatestRatesResponse mockResponse = new LatestRatesResponse();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/latest?base=IDR")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        LatestRatesResponse result = client.getLatestRates();

        assertEquals(mockResponse, result);
    }

    @Test
    void shouldReturnHistoricalRates() {

        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/2024-01-01..2024-01-05?from=IDR&to=USD")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        HistoricalRatesResponse result = client.getHistoricalRates();

        assertEquals(mockResponse, result);
    }

    @Test
    void shouldReturnCurrencies() {

        CurrenciesResponse mockResponse = new CurrenciesResponse();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/currencies")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrenciesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        CurrenciesResponse result = client.getCurrencies();

        assertEquals(mockResponse, result);
    }
}