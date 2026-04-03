package com.allo.test.unit.strategy;

import com.allo.finance.strategy.CurrencyFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyFetcherTest {

    @Mock WebClient webClient;
    @Mock WebClient.RequestHeadersUriSpec uriSpec;
    @Mock WebClient.RequestHeadersSpec headersSpec;
    @Mock WebClient.ResponseSpec responseSpec;

    @InjectMocks
    CurrencyFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchCurrencies() {
    
        Map<String, String> mock = Map.of("USD", "Dollar");
    
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    
        when(responseSpec.bodyToMono(Mockito.any(Class.class)))
                .thenReturn(Mono.just(mock));
    
        Object result = fetcher.fetch();
    
        assertEquals(mock, result);
    
        verify(webClient).get();
        verify(uriSpec).uri(anyString());
        verify(responseSpec).bodyToMono(any(Class.class));
    }
}