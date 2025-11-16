package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.CurrenciesResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private FrankfurterApiProperties properties;

    @Mock
    private FrankfurterApiProperties.Endpoints endpoints;

    @Mock
    private DataStoreService dataStoreService;

    @InjectMocks
    private SupportedCurrenciesStrategy strategy;


    @Test
    void testFetchFromExternalApi_Success() {
        // Arrange
        Map<String, String> currencies = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah",
                "JPY", "Japanese Yen"
        );

        CurrenciesResponse mockResponse = CurrenciesResponse.builder()
                .currencies(currencies)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrenciesResponse.class))
                .thenReturn(Mono.just(mockResponse));
        when(properties.getEndpoints()).thenReturn(endpoints);
        when(endpoints.getCurrencies()).thenReturn("/currencies");

        // Act
        Object result = strategy.fetchFromExternalApi();

        // Assert
        assertThat(result).isInstanceOf(CurrenciesResponse.class);
        CurrenciesResponse response = (CurrenciesResponse) result;

        assertThat(response.getCurrencies()).hasSize(4);
        assertThat(response.getCurrencies()).containsKey("USD");
        assertThat(response.getCurrencies()).containsKey("IDR");
        assertThat(response.getCurrencies().get("USD")).isEqualTo("United States Dollar");

        verify(webClient).get();
    }

    @Test
    void testGetData_RetrievesFromDataStore() {
        // Arrange
        CurrenciesResponse mockData = CurrenciesResponse.builder()
                .currencies(Map.of("USD", "United States Dollar"))
                .build();

        when(dataStoreService.getData("supported_currencies")).thenReturn(mockData);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result).isEqualTo(mockData);
        verify(dataStoreService).getData("supported_currencies");
    }

    @Test
    void testGetResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.SUPPORTED_CURRENCIES);
    }

    @Test
    void testGetStrategyName() {
        assertThat(strategy.getStrategyName()).isEqualTo("supported_currencies");
    }
}