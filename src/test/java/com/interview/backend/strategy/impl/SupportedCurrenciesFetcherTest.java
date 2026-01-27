package com.interview.backend.strategy.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;

class SupportedCurrenciesFetcherTest {

    private RestTemplate restTemplate;
    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new SupportedCurrenciesFetcher(restTemplate);
        ReflectionTestUtils.setField(fetcher, "baseUrl", "https://api.frankfurter.app");
    }

    @Test
    @DisplayName("Returns currencies map and count")
    void testFetchDataReturnsCurrenciesAndCount() {
        Map<String, String> currencies = new LinkedHashMap<>();
        currencies.put("USD", "United States Dollar");
        currencies.put("EUR", "Euro");
        currencies.put("JPY", "Japanese Yen");

        Mockito.when(restTemplate.getForObject(startsWith("https://api.frankfurter.app/currencies"), eq(Map.class)))
                .thenReturn(currencies);

        Map<String, Object> result = fetcher.fetchData();
        @SuppressWarnings("unchecked")
        Map<String, String> returnedCurrencies = (Map<String, String>) result.get("currencies");
        assertEquals(currencies, returnedCurrencies);
        assertEquals(3, result.get("count"));
    }

    @Test
    @DisplayName("Throws when currencies response is null")
    void testFetchDataNullResponse() {
        Mockito.when(restTemplate.getForObject(startsWith("https://api.frankfurter.app/currencies"), eq(Map.class)))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, fetcher::fetchData);
        assertTrue(ex.getMessage().contains("Failed to fetch supported currencies"));
    }
}
