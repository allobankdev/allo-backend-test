package com.interview.backend.strategy.impl;

import com.interview.backend.models.ExchangeRateResponse;
import com.interview.backend.utils.SpreadFactorUtil;
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

class LatestIdrRatesFetcherTest {

    private RestTemplate restTemplate;
    private LatestIdrRatesFetcher fetcher;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        fetcher = new LatestIdrRatesFetcher(restTemplate);
        ReflectionTestUtils.setField(fetcher, "baseUrl", "https://api.frankfurter.app");
        ReflectionTestUtils.setField(fetcher, "defaultGithubUsername", "alfin");
    }

    @Test
    @DisplayName("Formats rates and calculates USD buy spread with spread factor")
    void testFetchDataFormatsRatesAndCalculatesSpread() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase("IDR");
        response.setDate("2026-01-27");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000064d); // Example USD rate for base IDR
        rates.put("EUR", 0.000059d);
        response.setRates(rates);

        Mockito.when(restTemplate.getForObject(startsWith("https://api.frankfurter.app/latest?base=IDR"),
                eq(ExchangeRateResponse.class)))
                .thenReturn(response);

        Map<String, Object> result = fetcher.fetchData();

        assertEquals("IDR", result.get("base"));
        assertEquals("2026-01-27", result.get("date"));

        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> formattedRates = (Map<String, BigDecimal>) result.get("rates");
        assertNotNull(formattedRates);
        assertEquals(new BigDecimal("0.00006400"), formattedRates.get("USD"));
        assertEquals(new BigDecimal("0.00005900"), formattedRates.get("EUR"));

        double usdRate = rates.get("USD");
        double expectedSpreadFactor = SpreadFactorUtil.calculateSpreadFactor("alfin");
        double expectedUsdBuySpreadIdr = (1 / usdRate) * (1 + expectedSpreadFactor);

        assertEquals(expectedSpreadFactor, (Double) result.get("spread_factor"), 1e-12);
        assertEquals(expectedUsdBuySpreadIdr, (Double) result.get("USD_BuySpread_IDR"), 1e-12);
        assertEquals("alfin", result.get("github_username"));
        assertEquals(usdRate, (Double) result.get("usd_rate"), 1e-12);
    }

    @Test
    @DisplayName("Handles missing USD rate without adding spread-related fields")
    void testFetchDataWithoutUsdRate() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase("IDR");
        response.setDate("2026-01-27");
        Map<String, Double> rates = new HashMap<>();
        rates.put("JPY", 0.0098d);
        response.setRates(rates);

        Mockito.when(restTemplate.getForObject(startsWith("https://api.frankfurter.app/latest?base=IDR"),
                eq(ExchangeRateResponse.class)))
                .thenReturn(response);

        Map<String, Object> result = fetcher.fetchData();
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> formattedRates = (Map<String, BigDecimal>) result.get("rates");
        assertNotNull(formattedRates);
        assertNull(result.get("USD_BuySpread_IDR"));
        assertNull(result.get("spread_factor"));
        assertNull(result.get("github_username"));
        assertNull(result.get("usd_rate"));
    }

    @Test
    @DisplayName("Throws when response or rates are null")
    void testFetchDataNullResponse() {
        Mockito.when(restTemplate.getForObject(startsWith("https://api.frankfurter.app/latest?base=IDR"),
                eq(ExchangeRateResponse.class)))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> fetcher.fetchData());
        assertTrue(ex.getMessage().contains("Failed to fetch latest IDR rates"));
    }
}
