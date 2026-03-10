package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.dto.LatestRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestRatesStrategy latestRatesStrategy;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/latest?base=IDR")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchData_calculatesSpreadCorrectly() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setRates(Map.of("USD", 0.000065));

        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        Mono<Object> result = latestRatesStrategy.fetchData();

        StepVerifier.create(result)
                .expectNextMatches(responseObject -> {
                    LatestRatesResponse response = (LatestRatesResponse) responseObject;
                    double expectedSpread = (1 / 0.000065) * (1 + 0.00842);
                    return response.getUSD_BuySpread_IDR() != null &&
                           Math.abs(response.getUSD_BuySpread_IDR() - expectedSpread) < 0.0001;
                })
                .verifyComplete();
    }
}
