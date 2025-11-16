package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.HistoricalRatesResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
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
    private FrankfurterApiProperties properties;

    @Mock
    private FrankfurterApiProperties.Endpoints endpoints;

    @Mock
    private DataStoreService dataStoreService;

    @InjectMocks
    private HistoricalIDRUSDStrategy strategy;


    @Test
    void testFetchFromExternalApi_Success() {
        // Arrange
        Map<LocalDate, Map<String, BigDecimal>> rates = Map.of(
                LocalDate.of(2024, 1, 1), Map.of("USD", new BigDecimal("0.000064")),
                LocalDate.of(2024, 1, 2), Map.of("USD", new BigDecimal("0.000065")),
                LocalDate.of(2024, 1, 3), Map.of("USD", new BigDecimal("0.000066"))
        );

        HistoricalRatesResponse mockResponse = HistoricalRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .rates(rates)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));
        when(properties.getEndpoints()).thenReturn(endpoints);
        when(endpoints.getHistoricalIdrUsd()).thenReturn("/2024-01-01..2024-01-05?from=IDR&to=USD");

        // Act
        Object result = strategy.fetchFromExternalApi();

        // Assert
        assertThat(result).isInstanceOf(HistoricalRatesResponse.class);
        HistoricalRatesResponse response = (HistoricalRatesResponse) result;

        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 5));
        assertThat(response.getRates()).hasSize(3);

        verify(webClient).get();
    }

    @Test
    void testGetData_RetrievesFromDataStore() {
        // Arrange
        HistoricalRatesResponse mockData = HistoricalRatesResponse.builder()
                .base("IDR")
                .build();

        when(dataStoreService.getData("historical_idr_usd")).thenReturn(mockData);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result).isEqualTo(mockData);
        verify(dataStoreService).getData("historical_idr_usd");
    }

    @Test
    void testGetResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.HISTORICAL_IDR_USD);
    }

    @Test
    void testGetStrategyName() {
        assertThat(strategy.getStrategyName()).isEqualTo("historical_idr_usd");
    }
}