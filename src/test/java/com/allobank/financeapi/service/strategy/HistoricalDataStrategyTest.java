package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.dto.HistoricalRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalDataStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HistoricalDataStrategy historicalDataStrategy;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/2024-01-01..2024-01-05?from=IDR&to=USD")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchData_returnsHistoricalData() {
        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        // Populate mockResponse with some data if needed

        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        Mono<Object> result = historicalDataStrategy.fetchData();

        StepVerifier.create(result)
                .expectNext(mockResponse)
                .verifyComplete();
    }
}
