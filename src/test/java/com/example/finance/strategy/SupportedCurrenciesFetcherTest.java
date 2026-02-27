package com.example.finance.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SupportedCurrenciesFetcherTest {
    private RestTemplate restTemplate;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new SupportedCurrenciesFetcher(restTemplate);
    }

    @Test
    void fetchData_shouldConvertMapToList() {
        Map<String, String> apiResponse = Map.of("USD", "United States Dollar", "EUR", "Euro");
        when(restTemplate.getForObject(anyString(), Mockito.eq(Map.class))).thenReturn(apiResponse);
        List<Map<String, Object>> data = fetcher.fetchData();
        assertThat(data).hasSize(2);
        assertThat(data.get(0)).containsKeys("currency", "name");
    }
}