package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.HistoricalRateEntry;
import com.prasetyahs.allo.finance.model.HistoricalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIDRFetcherTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private HistoricalIDRFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new HistoricalIDRFetcher();
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchAndProcess_TransformsMapToList() {
        Map<String, Map<String, Double>> rates = new TreeMap<>();
        rates.put("2024-01-02", Map.of("USD", 1.2));
        rates.put("2024-01-01", Map.of("USD", 1.1));

        HistoricalResponse mockResponse = new HistoricalResponse(
                "1.0", "IDR", "2024-01-01", "2024-01-05", rates);

        when(responseSpec.bodyToMono(HistoricalResponse.class)).thenReturn(Mono.just(mockResponse));

        Object result = fetcher.fetchAndProcess(webClient);

        assertNotNull(result);
        List<HistoricalRateEntry> list = (List<HistoricalRateEntry>) result;

        assertEquals(2, list.size());
        assertEquals("2024-01-01", list.get(0).date()); // Sorted
        assertEquals(1.1, list.get(0).rate());
        assertEquals("2024-01-02", list.get(1).date());
    }
}
