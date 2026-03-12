package com.allobank.finace.service.strategy;

import com.allobank.finace.properties.FrankfurterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIdrUsdStrategy strategy;

    @BeforeEach
    void setUp() {
        FrankfurterProperties properties = new FrankfurterProperties();
        FrankfurterProperties.Historical historical = new FrankfurterProperties.Historical();
        historical.setStartDate("2024-01-01");
        historical.setEndDate("2024-01-05");
        properties.setHistorical(historical);

        strategy = new HistoricalIdrUsdStrategy(properties);
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(strategy.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnSortedListByDate() {
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "rates", Map.of(
                        "2024-01-03", Map.of("USD", 0.000064),
                        "2024-01-01", Map.of("USD", 0.000063),
                        "2024-01-02", Map.of("USD", 0.000063)
                )
        );

        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).get("date")).isEqualTo("2024-01-01");
        assertThat(result.get(1).get("date")).isEqualTo("2024-01-02");
        assertThat(result.get(2).get("date")).isEqualTo("2024-01-03");
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnEmptyList_whenResponseIsNull() {
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.empty());

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).isEmpty();
    }
}
