package com.allobank.strategy.impl;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.LatestRatesResponse;
import com.allobank.util.SpreadCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
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
class LatestIdrRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestIdrRatesStrategy strategy;
    private FrankfurterApiProperties apiProperties;

    @BeforeEach
    void setUp() {
        apiProperties = new FrankfurterApiProperties();
        apiProperties.setBaseUrl("https://api.frankfurter.app");
        
        strategy = new LatestIdrRatesStrategy(webClient, apiProperties);
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.now())
                .rates(Map.of("USD", new BigDecimal("0.000064")))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(strategy.fetchData().cast(LatestRatesResponse.class))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("IDR", response.getBase());
                    assertNotNull(response.getUsdBuySpreadIdr());
                    assertTrue(response.getUsdBuySpreadIdr().compareTo(BigDecimal.ZERO) > 0);
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
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(WebClientResponseException.class)
                .verify();

        verify(webClient).get();
    }

    @Test
    void testEnrichWithSpread_Calculation() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.000064");
        LatestRatesResponse response = LatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.now())
                .rates(Map.of("USD", usdRate))
                .build();

        // Act
        LatestRatesResponse enriched = (LatestRatesResponse) ReflectionTestUtils.invokeMethod(
                strategy, "enrichWithSpread", response);

        // Assert
        assertNotNull(enriched.getUsdBuySpreadIdr());
        BigDecimal spreadFactor = SpreadCalculator.calculateSpreadFactor("testuser");
        BigDecimal expected = SpreadCalculator.calculateUsdBuySpreadIdr(usdRate, spreadFactor);
        assertEquals(expected, enriched.getUsdBuySpreadIdr());
    }

    @Test
    void testGetResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }
}

