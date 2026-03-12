package com.allobank.finace.service.strategy;

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
import static org.assertj.core.data.Offset.offset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LatestIdrRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LatestIdrRatesStrategy("DoniOctopus");
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(strategy.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    void calculateSpreadFactor_shouldReturnCorrectValue() {
        // "donioctopus" ASCII sum = 1207, 1207 % 1000 = 207, 207 / 100000.0 = 0.00207
        double factor = strategy.calculateSpreadFactor("DoniOctopus");
        assertThat(factor).isEqualTo(0.00207);
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnDataWithUsdBuySpreadIdr() {
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "date", "2024-01-05",
                "amount", 1.0,
                "rates", Map.of("USD", 0.000064, "EUR", 0.000059)
        );

        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/latest?base=IDR")).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsKey("USD_BuySpread_IDR");

        double usdBuySpreadIdr = (double) result.get(0).get("USD_BuySpread_IDR");
        double expectedValue = (1.0 / 0.000064) * (1.0 + 0.00207);
        assertThat(usdBuySpreadIdr).isCloseTo(expectedValue, offset(0.01));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnEmptyList_whenResponseIsNull() {
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/latest?base=IDR")).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.empty());

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).isEmpty();
    }
}
