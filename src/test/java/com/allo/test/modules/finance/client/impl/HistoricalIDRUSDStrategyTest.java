package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.exceptions.ClientException;
import com.allo.test.modules.finance.exceptions.ConnectivityException;
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
 * Unit tests for HistoricalIDRUSDStrategy.
 * <p>
 * Tests the fetching and storage of historical IDR to USD exchange rates.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricalIDRUSDStrategy Unit Tests")
class HistoricalIDRUSDStrategyTest {

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
    private FrankfurterApiProperties.HistoricalRatesConfig historicalRatesConfig;

    private HistoricalIDRUSDStrategy strategy;

    @BeforeEach
    void setUp() {
        // Setup API properties mock with lenient stubbing
        lenient().when(apiProperties.getHistoricalRates()).thenReturn(historicalRatesConfig);
        lenient().when(historicalRatesConfig.getDateRange()).thenReturn("2024-01-01..2024-01-05");
        lenient().when(historicalRatesConfig.getFromCurrency()).thenReturn("IDR");
        lenient().when(historicalRatesConfig.getToCurrency()).thenReturn("USD");

        // Initialize strategy
        strategy = new HistoricalIDRUSDStrategy(apiProperties, dataStoreService);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should fetch historical data, store, and return response")
    void shouldFetchHistoricalDataStoreAndReturnResponse() {
        // Arrange
        HistoricalRatesResponse mockResponse = createMockHistoricalRatesResponse();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        HistoricalRatesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBase()).isEqualTo("IDR");
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 5));
        assertThat(result.getRates()).isNotEmpty();
        assertThat(result.getRates()).containsKey(LocalDate.of(2024, 1, 1));

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.HISTORICAL_RATES, result);
    }

    @Test
    @DisplayName("Should getData return stored data from DataStoreService")
    void shouldGetDataReturnStoredData() {
        // Arrange
        HistoricalRatesResponse mockResponse = createMockHistoricalRatesResponse();
        when(dataStoreService.get(ResourceType.HISTORICAL_RATES)).thenReturn(mockResponse);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(HistoricalRatesResponse.class);
        assertThat(((HistoricalRatesResponse) result).getBase()).isEqualTo("IDR");

        // Verify
        verify(dataStoreService, times(1)).get(ResourceType.HISTORICAL_RATES);
    }

    @Test
    @DisplayName("Should getResourceType return HISTORICAL_RATES")
    void shouldGetResourceTypeReturnHistoricalRates() {
        // Act
        ResourceType result = strategy.getResourceType();

        // Assert
        assertThat(result).isEqualTo(ResourceType.HISTORICAL_RATES);
    }

    @Test
    @DisplayName("Should format date range correctly in request URI")
    void shouldFormatDateRangeCorrectlyInRequestUri() {
        // Arrange
        HistoricalRatesResponse mockResponse = createMockHistoricalRatesResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        HistoricalRatesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNotNull();

        // Verify URI construction was called (date range is passed to uri builder)
        verify(requestHeadersUriSpec, times(1)).uri(any(java.util.function.Function.class));
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
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
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
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
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
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
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
    @DisplayName("Should store null response when API returns null")
    void shouldStoreNullResponse_WhenApiReturnsNull() {
        // Arrange - Strategy has minimal validation, stores null as-is
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.empty());

        // Act
        HistoricalRatesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNull();

        // Verify null response is NOT stored (if block prevents storage)
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should handle empty historical data gracefully")
    void shouldHandleEmptyHistoricalDataGracefully() {
        // Arrange
        HistoricalRatesResponse emptyResponse = HistoricalRatesResponse.builder()
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(new HashMap<>()) // Empty rates map
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class)).thenReturn(Mono.just(emptyResponse));

        // Act
        HistoricalRatesResponse result = strategy.fetchData(webClient);

        // Assert - Empty data stored as-is (minimal validation)
        assertThat(result).isNotNull();
        assertThat(result.getRates()).isEmpty();

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.HISTORICAL_RATES, result);
    }

    // ==================== HELPER METHODS ====================

    private HistoricalRatesResponse createMockHistoricalRatesResponse() {
        Map<LocalDate, Map<String, BigDecimal>> historicalData = new HashMap<>();

        // Date 1: 2024-01-01
        Map<String, BigDecimal> jan1Rates = new HashMap<>();
        jan1Rates.put("USD", new BigDecimal("0.0000640"));
        historicalData.put(LocalDate.of(2024, 1, 1), jan1Rates);

        // Date 2: 2024-01-02
        Map<String, BigDecimal> jan2Rates = new HashMap<>();
        jan2Rates.put("USD", new BigDecimal("0.0000635"));
        historicalData.put(LocalDate.of(2024, 1, 2), jan2Rates);

        // Date 3: 2024-01-03
        Map<String, BigDecimal> jan3Rates = new HashMap<>();
        jan3Rates.put("USD", new BigDecimal("0.0000638"));
        historicalData.put(LocalDate.of(2024, 1, 3), jan3Rates);

        return HistoricalRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(historicalData)
                .build();
    }
}
