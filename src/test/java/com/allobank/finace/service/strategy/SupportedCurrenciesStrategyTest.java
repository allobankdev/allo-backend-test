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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SupportedCurrenciesStrategy();
    }

    @Test
    void getResourceType_shouldReturnCorrectType() {
        assertThat(strategy.getResourceType()).isEqualTo("supported_currencies");
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnSortedListWithCodeAndName() {
        Map<String, String> mockResponse = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "IDR", "Indonesian Rupiah"
        );

        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).hasSize(3);
        assertThat(result).allSatisfy(entry -> assertThat(entry).containsKeys("code", "name"));
        assertThat(result.get(0).get("code")).isEqualTo("EUR");
        assertThat(result.get(1).get("code")).isEqualTo("IDR");
        assertThat(result.get(2).get("code")).isEqualTo("USD");
    }

    @SuppressWarnings("unchecked")
    @Test
    void fetch_shouldReturnEmptyList_whenResponseIsNull() {
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/currencies")).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.empty());

        List<Map<String, Object>> result = strategy.fetch(webClient);

        assertThat(result).isEmpty();
    }
}
