package com.allobank.exercise.api.service;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.ApiResponse;
import com.allobank.exercise.api.dto.ExchangeRate;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import com.allobank.exercise.api.service.impl.LatestIdrRateFetcher;
import com.allobank.exercise.api.util.CalculatorFinance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LatestIdrRateFetcherTest {

    @Test
    void testGetDataReturnsCorrectResponse() {
        ResourceCache resourceCache = Mockito.mock(ResourceCache.class);
        CalculatorFinance calculatorFinance = Mockito.mock(CalculatorFinance.class);

        ExchangeRateResponse mockCacheResponse = new ExchangeRateResponse();
        mockCacheResponse.setBase("IDR");
        mockCacheResponse.setAmount(BigDecimal.ONE);
        mockCacheResponse.setDate("2025-11-19");
        mockCacheResponse.setRates(Map.of(
                "USD", new BigDecimal("0.000060")
        ));

        when(resourceCache.getDataCache(ResourceType.LATEST_IDR_RATES))
                .thenReturn(mockCacheResponse);

        when(calculatorFinance.calculateUSDBuySpreadIDR(new BigDecimal("0.000060")))
                .thenReturn(new BigDecimal("0.000061"));

        LatestIdrRateFetcher fetcher =
                new LatestIdrRateFetcher(calculatorFinance, resourceCache);

        ApiResponse<Object> response = fetcher.getData();

        assertNotNull(response);
        assertEquals("success", response.getStatus());

        ExchangeRate data = (ExchangeRate) response.getData();
        assertNotNull(data);

        assertEquals("IDR", data.getBase());
        assertEquals(BigDecimal.ONE, data.getAmount());
        assertEquals("2025-11-19", data.getDate());

        verify(resourceCache, times(1))
                .getDataCache(ResourceType.LATEST_IDR_RATES);

        verify(calculatorFinance, times(1))
                .calculateUSDBuySpreadIDR(new BigDecimal("0.000060"));
    }

    @Test
    void testGetDataThrowsWhenCacheIsNull() {
        ResourceCache resourceCache = Mockito.mock(ResourceCache.class);
        CalculatorFinance calculatorFinance = Mockito.mock(CalculatorFinance.class);

        when(resourceCache.getDataCache(ResourceType.LATEST_IDR_RATES))
                .thenReturn(null);

        LatestIdrRateFetcher fetcher =
                new LatestIdrRateFetcher(calculatorFinance, resourceCache);

        assertThrows(NullPointerException.class, fetcher::getData);

        verify(resourceCache, times(1))
                .getDataCache(ResourceType.LATEST_IDR_RATES);

        verifyNoInteractions(calculatorFinance);
    }
}
