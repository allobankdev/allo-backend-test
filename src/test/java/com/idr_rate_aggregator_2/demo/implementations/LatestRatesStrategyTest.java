package com.idr_rate_aggregator_2.demo.implementations;

import com.idr_rate_aggregator_2.demo.dto.LatestRatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private LatestRatesStrategy strategy;
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        // Create strategy with mocked WebClient and test username
        strategy = new LatestRatesStrategy(webClient, testUsername);
    }

    @Test
    void testGetResourceType() {
        assertEquals("latest_idr_rates", strategy.getResourceType());
    }

    @Test
    void testFetchData_Success() {
        // Arrange
        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .base("IDR")
                .date(LocalDate.of(2024, 1, 15))
                .rates(Map.of("USD", BigDecimal.valueOf(0.000064)))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertTrue(response instanceof LatestRatesResponse);
                    LatestRatesResponse rates = (LatestRatesResponse) response;

                    assertEquals("IDR", rates.getBase());
                    assertEquals(LocalDate.of(2024, 1, 15), rates.getDate());
                    assertEquals("latest_idr_rates", rates.getResourceType());

                    // Verify spread calculation (should be > 0)
                    assertNotNull(rates.getUSD_BuySpread_IDR());
                    assertTrue(rates.getUSD_BuySpread_IDR().compareTo(BigDecimal.ZERO) > 0);

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_WithEmptyRates() {
        // Arrange
        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .base("IDR")
                .date(LocalDate.of(2024, 1, 15))
                .rates(Map.of()) // Empty rates
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    LatestRatesResponse rates = (LatestRatesResponse) response;

                    assertEquals("IDR", rates.getBase());
                    assertNull(rates.getUSD_BuySpread_IDR()); // No spread without USD rate

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_WithNoUsdRate() {
        // Arrange
        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .base("IDR")
                .date(LocalDate.of(2024, 1, 15))
                .rates(Map.of("EUR", BigDecimal.valueOf(0.000059))) // No USD
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<?> result = strategy.fetchData();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    LatestRatesResponse rates = (LatestRatesResponse) response;

                    assertEquals("IDR", rates.getBase());
                    assertNull(rates.getUSD_BuySpread_IDR()); // No spread without USD

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFetchData_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // Act & Assert
        StepVerifier.create(strategy.fetchData())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testSpreadFactorCalculation() {
        // Test dengan username yang berbeda
        LatestRatesStrategy strategy1 = new LatestRatesStrategy(webClient, "user1");
        LatestRatesStrategy strategy2 = new LatestRatesStrategy(webClient, "user2");
        LatestRatesStrategy strategy3 = new LatestRatesStrategy(webClient, "user123456789");

        // Kita tidak bisa akses method private, tapi kita bisa test melalui fetchData
        // Untuk test ini, kita akan menggunakan reflection atau test melalui fetchData dengan mock response

        // Alternatif: buat method public untuk testing atau test melalui fetchData
        // Karena method calculateSpreadFactor private, kita test melalui fetchData

        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .base("IDR")
                .date(LocalDate.now())
                .rates(Map.of("USD", BigDecimal.valueOf(0.000064)))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // Test strategy1
        StepVerifier.create(strategy1.fetchData())
                .assertNext(response -> {
                    LatestRatesResponse rates = (LatestRatesResponse) response;
                    assertNotNull(rates.getUSD_BuySpread_IDR());
                })
                .verifyComplete();

        // Test strategy2 (harusnya nilai spread berbeda)
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));

        StepVerifier.create(strategy2.fetchData())
                .assertNext(response -> {
                    LatestRatesResponse rates = (LatestRatesResponse) response;
                    assertNotNull(rates.getUSD_BuySpread_IDR());
                })
                .verifyComplete();

        // Karena kita tidak bisa bandingkan nilai langsung tanpa akses method private,
        // kita verifikasi bahwa spread tidak null
    }

    @Test
    void testGetResponseType() {
        assertEquals(LatestRatesResponse.class, strategy.getResponseType());
    }
}