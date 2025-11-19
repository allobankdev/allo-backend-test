package com.allobank.exercise.api.service;
import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.ApiResponse;
import com.allobank.exercise.api.dto.ExchangeHistory;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.service.impl.HistoricalIdrUsdFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalIdrToUsdFetcherTest {

    @Test
    void testGetDataReturnsCorrectResponse() {
        ResourceCache resourceCache = Mockito.mock(ResourceCache.class);

        ExchangeHistoryResponse mockCacheResponse = new ExchangeHistoryResponse();
        mockCacheResponse.setBase("IDR");
        mockCacheResponse.setStart_date("2024-01-01");
        mockCacheResponse.setEnd_date("2024-01-05");

        when(resourceCache.getDataCache(ResourceType.HISTORICAL_IDR_USD))
                .thenReturn(mockCacheResponse);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(resourceCache);

        ApiResponse<Object> response = fetcher.getData();

        assertNotNull(response);
        assertEquals("success", response.getStatus());

        ExchangeHistory result = (ExchangeHistory) response.getData();
        assertNotNull(result);

        assertEquals("IDR", result.getBase());
        assertEquals("2024-01-01", result.getStartDate());
        assertEquals("2024-01-05", result.getEndDate());

        verify(resourceCache, times(1))
                .getDataCache(ResourceType.HISTORICAL_IDR_USD);
    }

    @Test
    void testGetDataThrowsWhenCacheIsNull() {
        ResourceCache resourceCache = Mockito.mock(ResourceCache.class);

        when(resourceCache.getDataCache(ResourceType.HISTORICAL_IDR_USD))
                .thenReturn(null);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(resourceCache);

        assertThrows(NullPointerException.class, fetcher::getData);

        verify(resourceCache, times(1))
                .getDataCache(ResourceType.HISTORICAL_IDR_USD);
    }
}

