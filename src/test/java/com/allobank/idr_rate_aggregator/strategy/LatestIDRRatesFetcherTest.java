package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.config.FrankfurterProperties;
import com.allobank.idr_rate_aggregator.config.SpreadProperties;
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
class LatestIDRRatesFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FrankfurterProperties properties;

    @Mock
    private SpreadProperties spreadProperties;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LatestIDRRatesFetcher fetcher;

    private FrankfurterProperties.Endpoints endpoints;
    private FrankfurterProperties.Params params;

    @BeforeEach
    void setUp() {
        endpoints = new FrankfurterProperties.Endpoints();
        endpoints.setLatest("/latest");

        params = new FrankfurterProperties.Params();
        params.setBase("IDR");

        when(properties.getEndpoints()).thenReturn(endpoints);
        when(properties.getParams()).thenReturn(params);
        when(spreadProperties.getFactor()).thenReturn(0.00191);
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientChain(Map<String, Object> responseBody) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(responseBody));
    }

    @Test
    void fetch_shouldReturnFinanceDataWithUsdBuySpread() {
        // Arrange
        Map<String, Object> mockRates = Map.of("USD", 5.9e-05);
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2026-04-02",
                "rates", mockRates
        );
        mockWebClientChain(mockResponse);

        // Act
        FinanceData result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("latest_idr_rates");
        assertThat(result.getData()).isInstanceOf(Map.class);

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertThat(data).containsKey("USD_BuySpread_IDR");
    }

    @Test
    void fetch_shouldCalculateUsdBuySpreadCorrectly() {
        // Arrange
        double rateUsd = 5.9e-05;
        double spreadFactor = 0.00191;
        double expectedSpread = (1.0 / rateUsd) * (1.0 + spreadFactor);

        Map<String, Object> mockRates = Map.of("USD", rateUsd);
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2026-04-02",
                "rates", mockRates
        );
        mockWebClientChain(mockResponse);

        // Act
        FinanceData result = fetcher.fetch();
        Map<String, Object> data = (Map<String, Object>) result.getData();

        // Assert
        assertThat((Double) data.get("USD_BuySpread_IDR"))
                .isCloseTo(expectedSpread, within(0.001));
    }

    @Test
    void fetch_shouldThrowException_whenResponseIsNull() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.empty());

        // Act & Assert
        assertThatThrownBy(() -> fetcher.fetch())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(fetcher.getResourceType()).isEqualTo("latest_idr_rates");
    }
}
