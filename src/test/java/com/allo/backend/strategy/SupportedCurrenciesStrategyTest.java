package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SupportedCurrenciesStrategy(webClient);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo("supported_currencies");
    }

    @Test
    void shouldFetchSupportedCurrencies() {
        Map<String, String> mockCurrencies = new HashMap<>();
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("EUR", "Euro");
        mockCurrencies.put("IDR", "Indonesian Rupiah");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockCurrencies));

        Object result = strategy.fetchData();

        assertThat(result).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, String> currencies = (Map<String, String>) result;
        assertThat(currencies).hasSize(3);
        assertThat(currencies).containsKey("USD");
        assertThat(currencies).containsKey("IDR");

        verify(webClient, times(1)).get();
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch supported currencies");
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch supported currencies");
    }
}
