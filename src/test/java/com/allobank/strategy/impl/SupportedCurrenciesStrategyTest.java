package com.allobank.strategy.impl;

import com.allobank.dto.CurrenciesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SupportedCurrenciesStrategy(webClient);
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        CurrenciesResponse mockResponse = CurrenciesResponse.builder()
                .currencies(Map.of(
                        "USD", "United States Dollar",
                        "IDR", "Indonesian Rupiah",
                        "EUR", "Euro"
                ))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrenciesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(strategy.fetchData().cast(CurrenciesResponse.class))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertNotNull(response.getCurrencies());
                    assertTrue(response.getCurrencies().containsKey("USD"));
                    assertTrue(response.getCurrencies().containsKey("IDR"));
                })
                .verifyComplete();

        verify(webClient).get();
    }

    @Test
    void testFetchData_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrenciesResponse.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(WebClientResponseException.class)
                .verify();

        verify(webClient).get();
    }

    @Test
    void testGetResourceType() {
        assertEquals("supported_currencies", strategy.getResourceType());
    }
}

