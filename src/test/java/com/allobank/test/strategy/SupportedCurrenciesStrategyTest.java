package com.allobank.test.strategy;

import com.allobank.test.config.FrankfurterProperties;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {

    @Mock
    private WebClient webClient;

    @Mock
    private FrankfurterProperties properties;

    @Mock
    private FrankfurterProperties.Endpoints endpoints;

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        lenient().when(properties.getEndpoints()).thenReturn(endpoints);
        lenient().when(endpoints.getCurrencies()).thenReturn("/mock-currencies");

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        lenient().when(webClient.get()).thenReturn(uriSpec);
        lenient().when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        lenient().when(headersSpec.retrieve()).thenReturn(responseSpec);

        Map<String, String> mockCurrencies = new HashMap<>();
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("IDR", "Indonesian Rupiah");

        lenient().when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(mockCurrencies));
        strategy = new SupportedCurrenciesStrategy(webClient, properties);
    }

    @Test
    void testFetchData_Success() throws Exception {
        CompletableFuture<?> future = strategy.fetchData();
        Map<String, String> result = (Map<String, String>) future.get();

        Assertions.assertEquals("supported_currencies", strategy.getResourceType());
        Assertions.assertEquals("Indonesian Rupiah", result.get("IDR"));
    }
}