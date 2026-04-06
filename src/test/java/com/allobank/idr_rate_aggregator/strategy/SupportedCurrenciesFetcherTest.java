package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.config.FrankfurterProperties;
import com.allobank.idr_rate_aggregator.model.FinanceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SupportedCurrenciesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FrankfurterProperties properties;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        FrankfurterProperties.Endpoints endpoints = new FrankfurterProperties.Endpoints();
        endpoints.setCurrencies("/currencies");

        when(properties.getEndpoints()).thenReturn(endpoints);
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnCurrenciesFinanceData() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah",
                "EUR", "Euro"
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act
        FinanceData result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("supported_currencies");
        assertThat(result.getData()).isInstanceOf(Map.class);

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertThat(data).containsKey("USD");
        assertThat(data).containsKey("IDR");
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(fetcher.getResourceType()).isEqualTo("supported_currencies");
    }
}