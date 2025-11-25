package com.example.financedata.fetcher;

import com.example.financedata.dto.HistoricalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrUsdFetcherTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchHistoricalRates() {

        Map<String, Object> raw = Map.of(
                "2024-01-01", Map.of("USD", 0.000065),
                "2024-01-02", Map.of("USD", 0.000066)
        );

        when(webClient.get().uri(anyString()).retrieve().bodyToMono(Map.class)).thenReturn(Mono.just(raw));

        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(raw));

        HistoricalDto dto = (HistoricalDto) fetcher.fetch().block();

        assertNotNull(dto);
        assertEquals("IDR", dto.getFrom());
        assertEquals("USD", dto.getTo());
        assertEquals(2, dto.getRaw().size());
    }
}
