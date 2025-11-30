package com.test.frankfurterexchangerate.service.implement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalIdrUsdFetcherTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private org.springframework.web.reactive.function.client.WebClient webClient; // not used but required by constructor

    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fetcher = new HistoricalIdrUsdFetcher(
                webClient,
                restTemplate
        );
    }

    @Test
    void testFetchData_ReturnsSortedHistoricalRates() throws Exception {

        // --- Mock JSON Response ----
        Map<String, Map<String, Double>> ratesMock = new HashMap<>();
        Map<String, Double> r1 = new HashMap<>();
        r1.put("USD", 0.000064);

        Map<String, Double> r2 = new HashMap<>();
        r2.put("USD", 0.000065);

        Map<String, Double> r3 = new HashMap<>();
        r3.put("USD", 0.000063);

        ratesMock.put("2024-01-03", r1);
        ratesMock.put("2024-01-01", r2);
        ratesMock.put("2024-01-02", r3);

        Map<String, Object> bodyMock = new HashMap<>();
        bodyMock.put("rates", ratesMock);

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(bodyMock, HttpStatus.OK);

        String expectedUrl = "https://api.frankfurter.app/2024-01-01..2024-01-05?from=IDR&to=USD";

        // ---- Mock restTemplate call ----
        when(restTemplate.getForEntity(expectedUrl, Map.class))
                .thenReturn(mockResponse);

        // ---- Execute ----
        List<Object> result = fetcher.fetchData();

        // ---- Assertions ----
        assertNotNull(result);
        assertEquals(3, result.size());

        // Check sorting order
        Map<String, Object> first = (Map<String, Object>) result.get(0);
        Map<String, Object> second = (Map<String, Object>) result.get(1);
        Map<String, Object> third = (Map<String, Object>) result.get(2);

        assertEquals("2024-01-01", first.get("date"));
        assertEquals("2024-01-02", second.get("date"));
        assertEquals("2024-01-03", third.get("date"));

        // Check values match
        assertEquals(0.000065, first.get("rate_IDR_to_USD"));
        assertEquals(0.000063, second.get("rate_IDR_to_USD"));
        assertEquals(0.000064, third.get("rate_IDR_to_USD"));

        // Verify REST call
        verify(restTemplate, times(1))
                .getForEntity(expectedUrl, Map.class);
    }
}