package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import com.allo.backend.model.HistoricalRates;
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
class HistoricalIDRUSDStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIDRUSDStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new HistoricalIDRUSDStrategy(webClient);
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo("historical_idr_usd");
    }

    @Test
    void shouldFetchHistoricalData() {
        Map<String, Map<String, Double>> rates = new HashMap<>();
        Map<String, Double> ratesForDate1 = new HashMap<>();
        ratesForDate1.put("USD", 0.000063);
        rates.put("2024-01-01", ratesForDate1);

        HistoricalRates mockResponse = HistoricalRates.builder()
                .base("IDR")
                .startDate("2024-01-01")
                .endDate("2024-01-05")
                .rates(rates)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRates.class)).thenReturn(Mono.just(mockResponse));

        Object result = strategy.fetchData();

        assertThat(result).isInstanceOf(HistoricalRates.class);
        HistoricalRates historicalRates = (HistoricalRates) result;
        assertThat(historicalRates.getBase()).isEqualTo("IDR");
        assertThat(historicalRates.getRates()).isNotEmpty();

        verify(webClient, times(1)).get();
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRates.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch historical IDR to USD rates");
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HistoricalRates.class)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch historical IDR to USD rates");
    }
}
