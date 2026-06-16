package com.allobank.strategy.impl;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.HistoricalRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIdrUsdStrategy strategy;
    private FrankfurterApiProperties apiProperties;

    @BeforeEach
    void setUp() {
        apiProperties = new FrankfurterApiProperties();
        apiProperties.setBaseUrl("https://api.frankfurter.app");
        
        var historical = new FrankfurterApiProperties.Historical();
        historical.setStartDate("2024-01-01");
        historical.setEndDate("2024-01-05");
        historical.setFromCurrency("IDR");
        historical.setToCurrency("USD");
        apiProperties.setHistorical(historical);
        
        strategy = new HistoricalIdrUsdStrategy(webClient, apiProperties);
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        HistoricalRatesResponse mockResponse = HistoricalRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(Map.of(
                        LocalDate.of(2024, 1, 1), Map.of("USD", new BigDecimal("0.000064")),
                        LocalDate.of(2024, 1, 2), Map.of("USD", new BigDecimal("0.000065"))
                ))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(strategy.fetchData().cast(HistoricalRatesResponse.class))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("IDR", response.getBase());
                    assertNotNull(response.getRates());
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
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
                .thenReturn(Mono.error(new WebClientResponseException(404, "Not Found", null, null, null)));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(WebClientResponseException.class)
                .verify();

        verify(webClient).get();
    }

    @Test
    void testGetResourceType() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
    }
}

