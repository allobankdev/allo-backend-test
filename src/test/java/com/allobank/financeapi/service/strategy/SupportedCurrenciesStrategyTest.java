package com.allobank.financeapi.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private SupportedCurrenciesStrategy supportedCurrenciesStrategy;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchData_returnsSupportedCurrencies() {
        Map<String, String> mockResponse = Map.of("USD", "United States Dollar", "EUR", "Euro");

        when(responseSpec.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})).thenReturn(Mono.just(mockResponse));

        Mono<Object> result = supportedCurrenciesStrategy.fetchData();

        StepVerifier.create(result)
                .expectNext(mockResponse)
                .verifyComplete();
    }
}
