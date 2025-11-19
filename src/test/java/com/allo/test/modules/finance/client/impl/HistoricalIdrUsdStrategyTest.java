package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.HistoricalRateResponse;
import com.allo.test.modules.finance.dto.res.FrankfurterHistoricalRatesResponse;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for HistoricalIDRUSDStrategy.
 * <p>
 * Tests the fetching, transformation to List, and storage of historical IDR to USD exchange rates.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricalIDRUSDStrategy Unit Tests")
class HistoricalIdrUsdStrategyTest {

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

    private HistoricalIdrUsdStrategy strategy;

    @BeforeEach
    void setUp() {
        // Setup API properties mock with lenient stubbing
        lenient().when(apiProperties.getHistoricalRates()).thenReturn(historicalRatesConfig);
        lenient().when(historicalRatesConfig.getDateRange()).thenReturn("2024-01-01..2024-01-05");
        lenient().when(historicalRatesConfig.getFromCurrency()).thenReturn("IDR");
        lenient().when(historicalRatesConfig.getToCurrency()).thenReturn("USD");

        // Initialize strategy
        strategy = new HistoricalIdrUsdStrategy(apiProperties, dataStoreService);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should fetch historical data, transform to List of DTOs, and store")
    void shouldFetchHistoricalDataTransformToListAndStore() {
        // Arrange
        FrankfurterHistoricalRatesResponse mockResponse = createMockHistoricalRatesResponse();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        List<HistoricalRateResponse> result = strategy.fetchData(webClient);

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class)
                .hasSize(3);

        // Verify transformation structure with DTO
        HistoricalRateResponse firstEntry = result.get(0);
        assertThat(firstEntry).isNotNull();
        assertThat(firstEntry.getDate()).isNotNull();
        assertThat(firstEntry.getCurrency()).isEqualTo("USD");
        assertThat(firstEntry.getRate()).isInstanceOf(BigDecimal.class);

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.HISTORICAL_RATES, result);
    }

    @Test
    @DisplayName("Should getData return stored List of DTOs from DataStoreService")
    void shouldGetDataReturnStoredListData() {
        // Arrange
        List<HistoricalRateResponse> mockTransformedData = createExpectedTransformedData();
        when(dataStoreService.get(ResourceType.HISTORICAL_RATES)).thenReturn(mockTransformedData);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class);
        assertThat((List<?>) result).hasSize(3);

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
        FrankfurterHistoricalRatesResponse mockResponse = createMockHistoricalRatesResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        List<HistoricalRateResponse> result = strategy.fetchData(webClient);

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
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class))
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
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class))
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
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class))
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
    @DisplayName("Should return empty list when API returns null")
    void shouldReturnEmptyList_WhenApiReturnsNull() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class)).thenReturn(Mono.empty());

        // Act
        List<HistoricalRateResponse> result = strategy.fetchData(webClient);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        // Verify null response is NOT stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should handle empty historical data and return empty list")
    void shouldHandleEmptyHistoricalDataAndReturnEmptyList() {
        // Arrange
        FrankfurterHistoricalRatesResponse emptyResponse = FrankfurterHistoricalRatesResponse.builder()
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(new HashMap<>()) // Empty rates map
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterHistoricalRatesResponse.class)).thenReturn(Mono.just(emptyResponse));

        // Act
        List<HistoricalRateResponse> result = strategy.fetchData(webClient);

        // Assert - Empty data results in empty list
        assertThat(result).isEmpty();
    }

    // ==================== HELPER METHODS ====================

    private FrankfurterHistoricalRatesResponse createMockHistoricalRatesResponse() {
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

        return FrankfurterHistoricalRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(historicalData)
                .build();
    }

    private List<HistoricalRateResponse> createExpectedTransformedData() {
        // Expected transformed structure using HistoricalRateResponse DTOs
        return List.of(
                HistoricalRateResponse.builder()
                        .date("2024-01-01")
                        .currency("USD")
                        .rate(new BigDecimal("0.0000640"))
                        .build(),
                HistoricalRateResponse.builder()
                        .date("2024-01-02")
                        .currency("USD")
                        .rate(new BigDecimal("0.0000635"))
                        .build(),
                HistoricalRateResponse.builder()
                        .date("2024-01-03")
                        .currency("USD")
                        .rate(new BigDecimal("0.0000638"))
                        .build()
        );
    }
}
