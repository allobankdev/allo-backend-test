package com.test.frankfurterexchangerate.service.implement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportedCurrenciesFetcherTest {
    private SupportedCurrenciesFetcher fetcher;
    private RestTemplate restTemplate;

    @Mock
    private WebClient client;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new SupportedCurrenciesFetcher(
                client,
                restTemplate
        );
    }

    @Test
    void testFetchData_ReturnsSortedCurrencies() throws Exception {

        // --- Mock response body ---
        Map<String, Object> currenciesMock = new HashMap<>();
        currenciesMock.put("USD", "United States Dollar");
        currenciesMock.put("IDR", "Indonesian Rupiah");
        currenciesMock.put("EUR", "Euro");

        ResponseEntity<Map> mockResponse =
                new ResponseEntity<>(currenciesMock, HttpStatus.OK);

        String expectedUrl = "https://api.frankfurter.app/currencies";

        when(restTemplate.getForEntity(expectedUrl, Map.class))
                .thenReturn(mockResponse);

        // ---- Execute ----
        List<Object> result = fetcher.fetchData();

        // ---- Assertions ----
        assertNotNull(result);
        assertEquals(3, result.size());

        // list sudah tersortir berdasar currency ascending
        Map<String, Object> first = (Map<String, Object>) result.get(0);
        Map<String, Object> second = (Map<String, Object>) result.get(1);
        Map<String, Object> third = (Map<String, Object>) result.get(2);

        assertEquals("EUR", first.get("currency"));
        assertEquals("IDR", second.get("currency"));
        assertEquals("USD", third.get("currency"));

        assertEquals("Euro", first.get("name"));
        assertEquals("Indonesian Rupiah", second.get("name"));
        assertEquals("United States Dollar", third.get("name"));

        // RestTemplate harus dipanggil sekali
        verify(restTemplate, times(1)).getForEntity(expectedUrl, Map.class);
    }
}