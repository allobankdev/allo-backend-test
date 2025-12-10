package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import com.allo.backend.model.LatestRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    private LatestIDRRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LatestIDRRatesStrategy(webClient);
        ReflectionTestUtils.setField(strategy, "githubUsername", "testuser");
    }

    @Test
    void shouldReturnCorrectResourceType() {
        assertThat(strategy.getResourceType()).isEqualTo("latest_idr_rates");
    }

    @Test
    void shouldFetchAndCalculateUsdBuySpread() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000063);
        rates.put("EUR", 0.000057);

        LatestRates mockResponse = LatestRates.builder()
                .base("IDR")
                .date("2024-01-01")
                .rates(rates)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRates.class)).thenReturn(Mono.just(mockResponse));

        Object result = strategy.fetchData();

        assertThat(result).isInstanceOf(LatestRates.class);
        LatestRates latestRates = (LatestRates) result;
        assertThat(latestRates.getBase()).isEqualTo("IDR");
        assertThat(latestRates.getUsdBuySpreadIdr()).isNotNull();
        assertThat(latestRates.getUsdBuySpreadIdr()).isGreaterThan(0);

        verify(webClient, times(1)).get();
    }

    @Test
    void shouldCalculateSpreadFactorCorrectly() {
        String username = "testuser";
        double spreadFactor = strategy.calculateSpreadFactor(username);

        int expectedSum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            expectedSum += (int) c;
        }
        double expectedSpreadFactor = (expectedSum % 1000) / 100000.0;

        assertThat(spreadFactor).isEqualTo(expectedSpreadFactor);
        assertThat(spreadFactor).isGreaterThanOrEqualTo(0.0);
        assertThat(spreadFactor).isLessThan(0.01);
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRates.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch latest IDR rates");
    }

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LatestRates.class)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> strategy.fetchData())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Failed to fetch latest IDR rates");
    }
}
