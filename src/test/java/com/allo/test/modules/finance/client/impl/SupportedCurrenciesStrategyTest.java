package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.dto.res.CurrencyResponse;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for SupportedCurrenciesStrategy.
 * <p>
 * Tests the fetching, transformation to List, and storage of supported currency symbols.
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
    @DisplayName("Should fetch currency list, transform to List of DTOs, and store")
    void shouldFetchCurrencyListTransformToListAndStore() {
        // Arrange
        Map<String, String> mockCurrencies = createMockCurrenciesMap();

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockCurrencies));

        // Act
        List<CurrencyResponse> result = strategy.fetchData(webClient);

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class)
                .hasSize(8);

        // Verify transformation structure with DTO
        CurrencyResponse firstEntry = result.get(0);
        assertThat(firstEntry).isNotNull();
        assertThat(firstEntry.getCode()).isInstanceOf(String.class);
        assertThat(firstEntry.getName()).isInstanceOf(String.class);

        // Verify storage
        verify(dataStoreService, times(1)).store(ResourceType.CURRENCIES, result);
    }

    @Test
    @DisplayName("Should getData return stored List of DTOs from DataStoreService")
    void shouldGetDataReturnStoredListData() {
        // Arrange
        List<CurrencyResponse> mockTransformedData = createExpectedTransformedData();
        when(dataStoreService.get(ResourceType.CURRENCIES)).thenReturn(mockTransformedData);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class);
        assertThat((List<?>) result).hasSize(8);

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
    @DisplayName("Should return empty list when API returns null")
    void shouldReturnEmptyList_WhenApiReturnsNull() {
        // Arrange - API returns null map using justOrEmpty
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.justOrEmpty(null));

        // Act
        List<CurrencyResponse> result = strategy.fetchData(webClient);

        // Assert - Null map results in empty list
        assertThat(result)
                .isNotNull()
                .isEmpty();

        // Verify no data stored
        verify(dataStoreService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Should handle empty currency list and return empty list")
    void shouldHandleEmptyCurrencyListAndReturnEmptyList() {
        // Arrange
        Map<String, String> emptyCurrencies = new HashMap<>();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(emptyCurrencies));

        // Act
        List<CurrencyResponse> result = strategy.fetchData(webClient);

        // Assert - Empty map results in empty list
        assertThat(result)
                .isNotNull()
                .isEmpty();

        // Verify empty list is NOT stored (implementation skips storage for empty lists)
        verify(dataStoreService, never()).store(any(), any());
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
        List<CurrencyResponse> result = strategy.fetchData(webClient);

        // Assert - Special characters preserved in transformation
        assertThat(result)
                .isNotNull()
                .hasSize(3);

        // Find EUR entry using DTO
        CurrencyResponse eurEntry = result.stream()
                .filter(entry -> "EUR".equals(entry.getCode()))
                .findFirst()
                .orElse(null);
        assertThat(eurEntry).isNotNull();
        assertThat(eurEntry.getName()).contains("€");

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

    private List<CurrencyResponse> createExpectedTransformedData() {
        // Expected transformed structure using CurrencyResponse DTOs
        return List.of(
                CurrencyResponse.builder().code("USD").name("United States Dollar").build(),
                CurrencyResponse.builder().code("EUR").name("Euro").build(),
                CurrencyResponse.builder().code("GBP").name("British Pound Sterling").build(),
                CurrencyResponse.builder().code("JPY").name("Japanese Yen").build(),
                CurrencyResponse.builder().code("CAD").name("Canadian Dollar").build(),
                CurrencyResponse.builder().code("AUD").name("Australian Dollar").build(),
                CurrencyResponse.builder().code("CHF").name("Swiss Franc").build(),
                CurrencyResponse.builder().code("CNY").name("Chinese Yuan").build()
        );
    }
}
