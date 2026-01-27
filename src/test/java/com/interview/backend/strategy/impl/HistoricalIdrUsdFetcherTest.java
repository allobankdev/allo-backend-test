package com.interview.backend.strategy.impl;

import com.interview.backend.models.TimeSeriesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;

class HistoricalIdrUsdFetcherTest {

    private RestTemplate restTemplate;
    private HistoricalIdrUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new HistoricalIdrUsdFetcher(restTemplate);
        ReflectionTestUtils.setField(fetcher, "baseUrl", "https://api.frankfurter.app");
        ReflectionTestUtils.setField(fetcher, "defaultStart", "2024-01-01");
        ReflectionTestUtils.setField(fetcher, "defaultEnd", "2024-01-05");
    }

    @Test
    @DisplayName("Transforms time series rates to BigDecimal map and returns metadata")
    void testFetchDataValidRange() {
        String start = "2024-01-01";
        String end = "2024-01-03";

        Map<String, Map<String, Double>> rates = new HashMap<>();
        Map<String, Double> day1 = new HashMap<>();
        day1.put("USD", 0.000065d);
        Map<String, Double> day2 = new HashMap<>();
        day2.put("USD", 0.000066d);
        Map<String, Double> day3 = new HashMap<>();
        day3.put("USD", 0.000067d);
        rates.put("2024-01-01", day1);
        rates.put("2024-01-02", day2);
        rates.put("2024-01-03", day3);

        TimeSeriesResponse response = new TimeSeriesResponse("IDR", start, end, rates);

        String expectedUrlPrefix = "https://api.frankfurter.app/" + start + ".." + end + "?from=IDR&to=USD";
        Mockito.when(restTemplate.getForObject(startsWith(expectedUrlPrefix), eq(TimeSeriesResponse.class)))
                .thenReturn(response);

        Map<String, Object> result = fetcher.fetchData(Map.of("start_date", start, "end_date", end));

        assertEquals("IDR", result.get("base"));
        assertEquals(start, result.get("start_date"));
        assertEquals(end, result.get("end_date"));

        @SuppressWarnings("unchecked")
        Map<String, Map<String, BigDecimal>> formatted = (Map<String, Map<String, BigDecimal>>) result.get("rates");
        assertNotNull(formatted);
        assertEquals(new BigDecimal("0.00006500"), formatted.get("2024-01-01").get("USD"));
        assertEquals(new BigDecimal("0.00006600"), formatted.get("2024-01-02").get("USD"));
        assertEquals(new BigDecimal("0.00006700"), formatted.get("2024-01-03").get("USD"));
    }

    @Test
    @DisplayName("Throws for end before start")
    void testFetchDataInvalidRangeOrder() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> fetcher.fetchData(Map.of("start_date", "2024-01-03", "end_date", "2024-01-01")));
        assertTrue(ex.getMessage().contains("Failed to fetch historical rates"));
    }

    @Test
    @DisplayName("Throws for invalid date format")
    void testFetchDataInvalidDateFormat() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> fetcher.fetchData(Map.of("start_date", "01-01-2024", "end_date", "2024-01-03")));
        assertTrue(ex.getMessage().contains("Failed to fetch historical rates"));
    }

    @Test
    @DisplayName("Throws when API returns null response")
    void testFetchDataNullResponse() {
        String start = "2024-01-01";
        String end = "2024-01-03";

        String expectedUrlPrefix = "https://api.frankfurter.app/" + start + ".." + end + "?from=IDR&to=USD";
        Mockito.when(restTemplate.getForObject(startsWith(expectedUrlPrefix), eq(TimeSeriesResponse.class)))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> fetcher.fetchData(Map.of("start_date", start, "end_date", end)));
        assertTrue(ex.getMessage().contains("Failed to fetch historical rates"));
    }
}
