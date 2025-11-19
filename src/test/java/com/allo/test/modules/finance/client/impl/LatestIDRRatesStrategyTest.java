package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.exceptions.ClientException;
import com.allo.test.modules.finance.exceptions.ConnectivityException;
import com.allo.test.modules.finance.exceptions.ResponseParsingException;
import com.allo.test.modules.finance.exceptions.ServerException;
import com.allo.test.modules.finance.service.DataStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for LatestIDRRatesStrategy.
 * <p>
 * Tests the fetching, transformation, and storage of latest IDR rates
 * with USD buy spread calculation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LatestIDRRatesStrategy Unit Tests")
class LatestIDRRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private DataStoreService dataStoreService;

    @Mock
    private FrankfurterApiProperties apiProperties;

    @Mock
    private FrankfurterApiProperties.LatestRatesConfig latestRatesConfig;

    private LatestIDRRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        // Setup API properties mock with lenient stubbing
        lenient().when(apiProperties.getGithubUsername()).thenReturn("frhn9");
        lenient().when(apiProperties.getLatestRates()).thenReturn(latestRatesConfig);
        lenient().when(latestRatesConfig.getEndpoint()).thenReturn("/latest");
        lenient().when(latestRatesConfig.getBaseCurrency()).thenReturn("IDR");

        // Initialize strategy
        strategy = new LatestIDRRatesStrategy(apiProperties, dataStoreService);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should fetch data, calculate spread, store, and return enhanced response")
    void shouldFetchDataCalculateSpreadStoreAndReturnEnhancedResponse() {
        // Arrange
        LatestRatesResponse baseResponse = createMockLatestRatesResponse();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(baseResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getRates()).containsKey("USD");
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isGreaterThan(BigDecimal.ZERO);

        // Verify USD buy spread calculation
        // frhn9: f=102, r=114, h=104, n=110, 9=57 -> sum=487
        // Spread factor: (487 % 1000) / 100000.0 = 0.00487
        // USD rate: 0.0000634
        // Expected spread: (1 / 0.0000634) * (1 + 0.00487) = 15849.68
        BigDecimal expectedSpread = new BigDecimal("15849.68");
        assertThat(result.getUsdBuySpreadIdr()).isEqualByComparingTo(expectedSpread);

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.LATEST_RATES, result);
    }

    @Test
    @DisplayName("Should getData return stored data from DataStoreService")
    void shouldGetDataReturnStoredData() {
        // Arrange
        LatestIDRRatesResponse mockResponse = new LatestIDRRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setUsdBuySpreadIdr(new BigDecimal("15849.68"));
        when(dataStoreService.get(ResourceType.LATEST_RATES)).thenReturn(mockResponse);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(LatestIDRRatesResponse.class);
        assertThat(((LatestIDRRatesResponse) result).getBase()).isEqualTo("IDR");

        // Verify
        verify(dataStoreService, times(1)).get(ResourceType.LATEST_RATES);
    }

    @Test
    @DisplayName("Should getResourceType return LATEST_RATES")
    void shouldGetResourceTypeReturnLatestRates() {
        // Act
        ResourceType result = strategy.getResourceType();

        // Assert
        assertThat(result).isEqualTo(ResourceType.LATEST_RATES);
    }

    @Test
    @DisplayName("Should calculate USD buy spread correctly with real SpreadCalculator")
    void shouldCalculateUsdBuySpreadCorrectlyWithRealSpreadCalculator() {
        // Arrange
        LatestRatesResponse baseResponse = createMockLatestRatesResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(baseResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Formula: (1 / usdRate) * (1 + spreadFactor)
        // = (1 / 0.0000634) * (1 + 0.00487)
        // = 15772.870662460568 * 1.00487
        // = 15849.68 (rounded to 2 decimal places)
        BigDecimal expectedSpread = new BigDecimal("15849.68");

        assertThat(result.getUsdBuySpreadIdr()).isEqualByComparingTo(expectedSpread);
        assertThat(result.getUsdBuySpreadIdr().scale()).isEqualTo(2);
    }

    // ==================== ERROR SCENARIOS ====================

    @Test
    @DisplayName("Should throw ResponseParsingException when API returns null response")
    void shouldThrowResponseParsingException_WhenApiReturnsNullResponse() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(ResponseParsingException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should throw ResponseParsingException when response has null rates map")
    void shouldThrowResponseParsingException_WhenResponseHasNullRatesMap() {
        // Arrange
        LatestRatesResponse baseResponse = new LatestRatesResponse();
        baseResponse.setBase("IDR");
        baseResponse.setRates(null); // Null rates map

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(baseResponse));

        // Act & Assert
        assertThrows(ResponseParsingException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should throw ClientException when WebClient returns 4xx error")
    void shouldThrowClientException_WhenWebClientReturns4xxError() {
        // Arrange
        WebClientResponseException badRequest = WebClientResponseException.create(
                400,
                "Bad Request",
                null,
                null,
                null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(badRequest));

        // Act & Assert
        assertThrows(ClientException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should throw ServerException when WebClient returns 5xx error")
    void shouldThrowServerException_WhenWebClientReturns5xxError() {
        // Arrange
        WebClientResponseException serverError = WebClientResponseException.create(
                500,
                "Internal Server Error",
                null,
                null,
                null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(serverError));

        // Act & Assert
        assertThrows(ServerException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should throw ConnectivityException when WebClient encounters timeout")
    void shouldThrowConnectivityException_WhenWebClientEncountersTimeout() {
        // Arrange
        WebClientRequestException requestException = mock(WebClientRequestException.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.error(requestException));

        // Act & Assert
        assertThrows(ConnectivityException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should handle zero USD rate gracefully")
    void shouldHandleZeroUsdRateGracefully() {
        // Arrange
        LatestRatesResponse baseResponse = createMockLatestRatesResponse();
        baseResponse.getRates().put("USD", BigDecimal.ZERO); // Zero USD rate

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(baseResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Assert - SpreadCalculator returns BigDecimal.ZERO for zero rate
        assertThat(result).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isEqualByComparingTo(BigDecimal.ZERO);

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.LATEST_RATES, result);
    }

    @Test
    @DisplayName("Should handle missing USD rate gracefully")
    void shouldHandleMissingUsdRateGracefully() {
        // Arrange
        LatestRatesResponse baseResponse = createMockLatestRatesResponse();
        baseResponse.getRates().remove("USD"); // Remove USD rate

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class)).thenReturn(Mono.just(baseResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Assert - SpreadCalculator returns BigDecimal.ZERO for null rate
        assertThat(result).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isEqualByComparingTo(BigDecimal.ZERO);

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.LATEST_RATES, result);
    }

    // ==================== HELPER METHODS ====================

    private LatestRatesResponse createMockLatestRatesResponse() {
        LatestRatesResponse response = new LatestRatesResponse();
        response.setAmount(BigDecimal.ONE);
        response.setBase("IDR");
        response.setDate(LocalDate.of(2025, 1, 19));

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.0000634"));
        rates.put("EUR", new BigDecimal("0.0000580"));
        rates.put("GBP", new BigDecimal("0.0000490"));
        response.setRates(rates);

        return response;
    }
}
