package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for SupportedCurrenciesStrategy.
 * <p>
 * Tests the fetching and storage of supported currency symbols.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SupportedCurrenciesStrategy Unit Tests")
class SupportedCurrenciesStrategyTest {

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
    private FrankfurterApiProperties.CurrenciesConfig currenciesConfig;

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        // Setup API properties mock with lenient stubbing
        lenient().when(apiProperties.getCurrencies()).thenReturn(currenciesConfig);
        lenient().when(currenciesConfig.getEndpoint()).thenReturn("/currencies");

        // Initialize strategy
        strategy = new SupportedCurrenciesStrategy(apiProperties, dataStoreService);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should fetch currency list, store, and return response")
    void shouldFetchCurrencyListStoreAndReturnResponse() {
        // Arrange
        Map<String, String> mockCurrencies = createMockCurrenciesMap();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockCurrencies));

        // Act
        CurrenciesResponse result = strategy.fetchData(webClient);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCurrencies()).isNotNull();
        assertThat(result.getCurrencies()).isNotEmpty();
        assertThat(result.getCurrencies()).containsKeys("USD", "EUR", "GBP", "JPY");
        assertThat(result.getCurrencies()).containsEntry("USD", "United States Dollar");

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.CURRENCIES, result);
    }

    @Test
    @DisplayName("Should getData return stored data from DataStoreService")
    void shouldGetDataReturnStoredData() {
        // Arrange
        CurrenciesResponse mockResponse = CurrenciesResponse.builder()
                .currencies(createMockCurrenciesMap())
                .build();
        when(dataStoreService.get(ResourceType.CURRENCIES)).thenReturn(mockResponse);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(CurrenciesResponse.class);
        assertThat(((CurrenciesResponse) result).getCurrencies()).isNotEmpty();

        // Verify
        verify(dataStoreService, times(1)).get(ResourceType.CURRENCIES);
    }

    @Test
    @DisplayName("Should getResourceType return CURRENCIES")
    void shouldGetResourceTypeReturnCurrencies() {
        // Act
        ResourceType result = strategy.getResourceType();

        // Assert
        assertThat(result).isEqualTo(ResourceType.CURRENCIES);
    }

    // ==================== ERROR SCENARIOS ====================

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
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
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
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
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
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
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
    @DisplayName("Should handle null currency map gracefully")
    void shouldHandleNullCurrencyMapGracefully() {
        // Arrange - API returns null map using justOrEmpty
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.justOrEmpty(null));

        // Act
        CurrenciesResponse result = strategy.fetchData(webClient);

        // Assert - Null map wrapped in response
        assertThat(result).isNotNull();
        assertThat(result.getCurrencies()).isNull();

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.CURRENCIES, result);
    }

    @Test
    @DisplayName("Should handle empty currency list gracefully")
    void shouldHandleEmptyCurrencyListGracefully() {
        // Arrange
        Map<String, String> emptyCurrencies = new HashMap<>();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(emptyCurrencies));

        // Act
        CurrenciesResponse result = strategy.fetchData(webClient);

        // Assert - Empty map stored as-is (minimal validation)
        assertThat(result).isNotNull();
        assertThat(result.getCurrencies()).isEmpty();

        // Verify storage still occurs
        verify(dataStoreService, times(1)).store(ResourceType.CURRENCIES, result);
    }

    @Test
    @DisplayName("Should handle special characters in currency codes and names")
    void shouldHandleSpecialCharactersInCurrencyCodesAndNames() {
        // Arrange
        Map<String, String> specialCurrencies = new HashMap<>();
        specialCurrencies.put("USD", "United States Dollar");
        specialCurrencies.put("EUR", "Euro (€)");
        specialCurrencies.put("GBP", "British Pound £");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(specialCurrencies));

        // Act
        CurrenciesResponse result = strategy.fetchData(webClient);

        // Assert - Special characters preserved
        assertThat(result).isNotNull();
        assertThat(result.getCurrencies().get("EUR")).contains("€");
        assertThat(result.getCurrencies().get("GBP")).contains("£");

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.CURRENCIES, result);
    }

    // ==================== HELPER METHODS ====================

    private Map<String, String> createMockCurrenciesMap() {
        Map<String, String> currencies = new HashMap<>();
        currencies.put("USD", "United States Dollar");
        currencies.put("EUR", "Euro");
        currencies.put("GBP", "British Pound Sterling");
        currencies.put("JPY", "Japanese Yen");
        currencies.put("CAD", "Canadian Dollar");
        currencies.put("AUD", "Australian Dollar");
        currencies.put("CHF", "Swiss Franc");
        currencies.put("CNY", "Chinese Yuan");
        return currencies;
    }
}
