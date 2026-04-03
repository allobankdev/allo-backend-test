package com.allo.test.unit.strategy;

import com.allo.finance.strategy.LatestRatesFetcher;
import com.allo.finance.util.SpreadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LatestRatesFetcherTest {

    @Mock WebClient webClient;
    @Mock WebClient.RequestHeadersUriSpec uriSpec;
    @Mock WebClient.RequestHeadersSpec headersSpec;
    @Mock WebClient.ResponseSpec responseSpec;
    @Mock SpreadUtil spreadUtil;

    @InjectMocks
    LatestRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {

        Map<String, Object> mock = new HashMap<>();
        mock.put("rates", Map.of("USD", 0.000065));

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mock));      
        when(spreadUtil.calculateSpread()).thenReturn(0.001);

        Map result = (Map) fetcher.fetch();

        double expected = (1 / 0.000065) * (1 + 0.001);

        assertEquals(expected, result.get("USD_BuySpread_IDR"));

        verify(webClient).get();
    }
}