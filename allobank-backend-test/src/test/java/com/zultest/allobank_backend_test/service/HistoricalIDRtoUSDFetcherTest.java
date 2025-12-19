package com.zultest.allobank_backend_test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoricalIDRtoUSDFetcherTest {

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HistoricalIDRtoUSDFetcher fetcher;

    @Test
    void shouldFetchLatestRates() {
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse()));

        Object result = fetcher.fetchData();

        assertNotNull(result);
    }

    private Map<String, Object> mockResponse() {
        Map<String, Object> day1 = new HashMap<>();
        day1.put("USD", 0.000064);

        Map<String, Object> day2 = new HashMap<>();
        day2.put("USD", 0.000065);

        Map<String, Object> rates = new HashMap<>();
        rates.put("2024-01-01", day1);
        rates.put("2024-01-02", day2);

        Map<String, Object> response = new HashMap<>();
        response.put("amount", 1.0);
        response.put("base", "IDR");
        response.put("start_date", "2024-01-01");
        response.put("end_date", "2024-01-05");
        response.put("rates", rates);

        return response;
    }
}
