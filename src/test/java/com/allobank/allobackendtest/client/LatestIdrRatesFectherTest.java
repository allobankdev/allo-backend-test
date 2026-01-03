package com.allobank.allobackendtest.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import com.allobank.allobackendtest.model.DTO.LatestIdrRatesResponse;
import com.allobank.allobackendtest.strategy.LatestIdrRatesFetcher;

import reactor.core.publisher.Mono;

@SpringBootTest
class LatestIdrRatesFectherTest {

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private ResponseSpec responseSpec;

    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fetcher = new LatestIdrRatesFetcher(webClient);
    }

    @Test
    void shouldFetchLatestIdrRatesSuccessfully() {
        LatestIdrRatesResponse mockResponse = new LatestIdrRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of("USD", new BigDecimal("6.0E-05"),"EUR", new BigDecimal("5.1e-05")));

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestIdrRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        Object result = fetcher.fetchData();

        assertThat(result).isInstanceOf(LatestIdrRatesResponse.class);
    }

}
