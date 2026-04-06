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
class HistoricalIDRUsdFetcherTest {

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
    private HistoricalIDRUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        FrankfurterProperties.Endpoints endpoints = new FrankfurterProperties.Endpoints();
        endpoints.setHistorical("/2024-01-01..2024-01-05");

        FrankfurterProperties.Params params = new FrankfurterProperties.Params();
        params.setFrom("IDR");
        params.setTo("USD");

        when(properties.getEndpoints()).thenReturn(endpoints);
        when(properties.getParams()).thenReturn(params);
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnHistoricalFinanceData() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "start_date", "2024-01-01",
                "end_date", "2024-01-05",
                "rates", Map.of(
                        "2024-01-02", Map.of("USD", 6.4e-05),
                        "2024-01-03", Map.of("USD", 6.4e-05)
                )
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act
        FinanceData result = fetcher.fetch();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResourceType()).isEqualTo("historical_idr_usd");
        assertThat(result.getData()).isInstanceOf(Map.class);

        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertThat(data).containsKey("rates");
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(fetcher.getResourceType()).isEqualTo("historical_idr_usd");
    }
}
