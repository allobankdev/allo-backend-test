package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.dto.res.FrankfurterLatestRatesResponse;
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

/**
 * Unit tests for LatestIDRRatesStrategy.
 * <p>
 * Tests the fetching, transformation, and storage of latest IDR exchange rates
 * with USD buy spread calculation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LatestIDRRatesStrategy Unit Tests")
class LatestIdrRatesStrategyTest {

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

    private LatestIdrRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        // Setup API properties mock with lenient stubbing
        lenient().when(apiProperties.getLatestRates()).thenReturn(latestRatesConfig);
        lenient().when(latestRatesConfig.getEndpoint()).thenReturn("/latest");
        lenient().when(latestRatesConfig.getBaseCurrency()).thenReturn("IDR");
        lenient().when(apiProperties.getGithubUsername()).thenReturn("frhn9");

        // Initialize strategy
        strategy = new LatestIdrRatesStrategy(apiProperties, dataStoreService);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should fetch rates, calculate spread, and return response")
    void shouldFetchRatesCalculateSpreadAndReturnResponse() {
        // Arrange
        FrankfurterLatestRatesResponse mockResponse = createMockLatestRatesResponse();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getUsdBuySpreadIdr()).isNotNull();
        assertThat(result.getUsdBuySpreadIdr()).isInstanceOf(BigDecimal.class);

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.LATEST_RATES, result);
    }

    @Test
    @DisplayName("Should getData return stored data from DataStoreService")
    void shouldGetDataReturnStoredData() {
        // Arrange
        LatestIDRRatesResponse mockResponse = createMockLatestIDRRatesResponse();
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

    // ==================== ERROR SCENARIOS ====================

    @Test
    @DisplayName("Should throw ClientException when WebClient returns 4xx error")
    void shouldThrowClientException_WhenWebClientReturns4xxError() {
        // Arrange
        WebClientResponseException badRequest = WebClientResponseException.create(
                404,
                "Not Found",
                null,
                null,
                null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class))
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
                503,
                "Service Unavailable",
                null,
                null,
                null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class))
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
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class))
                .thenReturn(Mono.error(requestException));

        // Act & Assert
        assertThrows(ConnectivityException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should throw ResponseParsingException when rates map is null")
    void shouldThrowResponseParsingException_WhenRatesMapIsNull() {
        // Arrange
        FrankfurterLatestRatesResponse nullRatesResponse = FrankfurterLatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.parse("2024-01-01"))
                .rates(null) // Null rates map
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class)).thenReturn(Mono.just(nullRatesResponse));

        // Act & Assert
        assertThrows(ResponseParsingException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should throw ResponseParsingException when API returns null")
    void shouldThrowResponseParsingException_WhenApiReturnsNull() {
        // Arrange - Implementation validates and throws exception for null response
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(ResponseParsingException.class, () -> {
            strategy.fetchData(webClient);
        });

        // Verify no data stored when null response received
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should handle empty rates gracefully")
    void shouldHandleEmptyRatesGracefully() {
        // Arrange
        FrankfurterLatestRatesResponse emptyRatesResponse = FrankfurterLatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.parse("2024-01-01"))
                .rates(new HashMap<>()) // Empty rates map
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterLatestRatesResponse.class)).thenReturn(Mono.just(emptyRatesResponse));

        // Act
        LatestIDRRatesResponse result = strategy.fetchData(webClient);

        // Assert - Empty data stored as-is (minimal validation)
        assertThat(result).isNotNull();
        assertThat(result.getRates()).isEmpty();

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.LATEST_RATES, result);
    }

    // ==================== HELPER METHODS ====================

    private FrankfurterLatestRatesResponse createMockLatestRatesResponse() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.000064"));
        rates.put("EUR", new BigDecimal("0.000055"));
        rates.put("GBP", new BigDecimal("0.000047"));

        return FrankfurterLatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.parse("2024-01-01"))
                .rates(rates)
                .build();
    }

    private LatestIDRRatesResponse createMockLatestIDRRatesResponse() {
        FrankfurterLatestRatesResponse baseResponse = createMockLatestRatesResponse();
        
        return LatestIDRRatesResponse.fromLatestRatesResponse()
                .base(baseResponse)
                .usdBuySpreadIdr(new BigDecimal("16747.83")) // Calculated spread
                .build();
    }
}
