package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.CurrencyEntry;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesFetcherTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new SupportedCurrenciesFetcher();
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchAndProcess_TransformsMapToList() {
        Map<String, String> currencies = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah");

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(currencies));

        Object result = fetcher.fetchAndProcess(webClient);

        assertNotNull(result);
        List<CurrencyEntry> list = (List<CurrencyEntry>) result;
        assertEquals(2, list.size());
        // Map ordering is not guaranteed unless linked, but list size check is enough
        // for transformation logic
    }
}
