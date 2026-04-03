package com.allo.test.unit.strategy;

import com.allo.finance.strategy.HistoricalFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalFetcherTest {

    @Mock WebClient webClient;
    @Mock WebClient.RequestHeadersUriSpec uriSpec;
    @Mock WebClient.RequestHeadersSpec headersSpec;
    @Mock WebClient.ResponseSpec responseSpec;

    @InjectMocks
    HistoricalFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchHistoricalData() {

        Map<String, Object> rates = new HashMap<>();
        rates.put("2024-01-01", Map.of("USD", 0.000065));

        Map<String, Object> mock = new HashMap<>();
        mock.put("rates", rates);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mock));

        Object result = fetcher.fetch();

        assertEquals(mock, result);
    }
}