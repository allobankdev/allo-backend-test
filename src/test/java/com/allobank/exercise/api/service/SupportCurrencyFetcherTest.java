package com.allobank.exercise.api.service;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.ApiResponse;
import com.allobank.exercise.api.dto.CurrencyInfo;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.service.impl.SupportedCurrencyFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportCurrencyFetcherTest {

    @Test
    void testGetDataReturnsCorrectResponse() {
        ResourceCache resourceCache = Mockito.mock(ResourceCache.class);

        Map<String, String> currencyMap = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        when(resourceCache.getDataCache(ResourceType.SUPPORTED_CURRENCIES))
                .thenReturn(currencyMap);

        SupportedCurrencyFetcher fetcher = new SupportedCurrencyFetcher(resourceCache);

        ApiResponse<Object> response = fetcher.getData();

        assertNotNull(response);
        assertEquals("success", response.getStatus());

        List<CurrencyInfo> resultList = (List<CurrencyInfo>) response.getData();
        assertEquals(2, resultList.size());

        assertTrue(
                resultList.stream().anyMatch(c -> c.getCode().equals("USD") && c.getName().equals("United States Dollar"))
        );

        assertTrue(
                resultList.stream().anyMatch(c -> c.getCode().equals("IDR") && c.getName().equals("Indonesian Rupiah"))
        );

        verify(resourceCache, times(1)).getDataCache(ResourceType.SUPPORTED_CURRENCIES);
    }
}

