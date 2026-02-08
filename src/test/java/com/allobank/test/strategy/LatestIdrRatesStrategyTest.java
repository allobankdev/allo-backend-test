package com.allobank.test.strategy;

import com.allobank.test.config.FrankfurterProperties;
import com.allobank.test.dto.FrankfurterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesStrategyTest {

    @Mock
    private WebClient webClient;

    private FrankfurterProperties properties;

    private LatestIdrRatesStrategy strategy;

    @BeforeEach
    void setUp() {
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        properties = new FrankfurterProperties();
        FrankfurterProperties.Endpoints endpoints = new FrankfurterProperties.Endpoints();
        endpoints.setLatest("/mock-url");
        properties.setEndpoints(endpoints);

        when(webClient.get()).thenReturn(uriSpec);

        when(uriSpec.uri(properties.getEndpoints().getLatest())).thenReturn(headersSpec);

        when(headersSpec.retrieve()).thenReturn(responseSpec);

        FrankfurterResponse mockResponse = new FrankfurterResponse();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-01");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000059);
        mockResponse.setRates(rates);

        when(responseSpec.bodyToMono(FrankfurterResponse.class)).thenReturn(Mono.just(mockResponse));

        double spreadFactor = 0.0026;
        strategy = new LatestIdrRatesStrategy(webClient, properties, spreadFactor);
    }

    @Test
    void testCalculateSpreadFactor() throws Exception {
        CompletableFuture<?> future = strategy.fetchData();
        Map<String, Object> result = (Map<String, Object>) future.get();
        Map<String, Double> rates = (Map<String, Double>) result.get("rates");

        Assertions.assertTrue(rates.containsKey("USD_BuySpread_IDR"), "Field USD_BuySpread_IDR harus ada");

        double actualRate = rates.get("USD_BuySpread_IDR");
        double expectedRate = (1 / 0.000059) * (1 + 0.0026);

        Assertions.assertEquals(expectedRate, actualRate, 0.0001, "Perhitungan Spread Factor salah!");
    }
}