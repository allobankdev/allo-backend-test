package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.LatestRatesResponse;
import com.allobank.dto.LatestRatesWithSpreadResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import com.allobank.utility.SpreadCalculator;
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
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private SpreadCalculator spreadCalculator;

    @Mock
    private FrankfurterApiProperties properties;

    @Mock
    private FrankfurterApiProperties.Endpoints endpoints;

    @Mock
    private DataStoreService dataStoreService;

    @InjectMocks
    private LatestIDRRatesStrategy strategy;


    @Test
    void testFetchFromExternalApi_Success() {
        // Arrange
        LatestRatesResponse mockResponse = LatestRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .date(LocalDate.of(2024, 1, 15))
                .rates(Map.of(
                        "USD", new BigDecimal("0.000064"),
                        "EUR", new BigDecimal("0.000059")
                ))
                .build();

        BigDecimal mockSpreadFactor = new BigDecimal("0.00765");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRatesResponse.class))
                .thenReturn(Mono.just(mockResponse));
        when(spreadCalculator.calculateSpreadFactor()).thenReturn(mockSpreadFactor);
        when(properties.getEndpoints()).thenReturn(endpoints);
        when(endpoints.getLatestIdr()).thenReturn("/latest?base=IDR");

        // Act
        Object result = strategy.fetchFromExternalApi();

        // Assert
        assertThat(result).isInstanceOf(LatestRatesWithSpreadResponse.class);
        LatestRatesWithSpreadResponse response = (LatestRatesWithSpreadResponse) result;

        assertThat(response.getBase()).isEqualTo("IDR");
        assertThat(response.getRates()).containsKey("USD");
        assertThat(response.getUsdBuySpreadIdr()).isNotNull();
        assertThat(response.getSpreadFactor()).isEqualTo(mockSpreadFactor);

        // Verify calculation: (1 / 0.000064) * (1 + 0.00765)
        BigDecimal expected = new BigDecimal("15744.53125");
        assertThat(response.getUsdBuySpreadIdr())
                .isCloseTo(expected, within(new BigDecimal("0.01")));

        verify(webClient).get();
        verify(spreadCalculator).calculateSpreadFactor();
    }

    @Test
    void testGetData_RetrievesFromDataStore() {
        // Arrange
        LatestRatesWithSpreadResponse mockData = LatestRatesWithSpreadResponse.builder()
                .base("IDR")
                .build();

        when(dataStoreService.getData("latest_idr_rates")).thenReturn(mockData);

        // Act
        Object result = strategy.getData();

        // Assert
        assertThat(result).isEqualTo(mockData);
        verify(dataStoreService).getData("latest_idr_rates");
    }

    @Test
    void testGetResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo(ResourceType.LATEST_IDR_RATES);
    }

    @Test
    void testGetStrategyName() {
        assertThat(strategy.getStrategyName()).isEqualTo("latest_idr_rates");
    }
}