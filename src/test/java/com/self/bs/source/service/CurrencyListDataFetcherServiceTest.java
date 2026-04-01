package com.self.bs.source.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.webclient.ExchangeRateWebClient;

@ExtendWith(MockitoExtension.class)
public class CurrencyListDataFetcherServiceTest {
    @InjectMocks
    protected CurrencyListDataFetcherService currencyListDataFetcherService;

    @Mock
    protected ExchangeRateWebClient exchangeRateWebClient;

    @Mock
    protected ConcurrentMapCacheManager cacheManager;

    @Mock
    protected ExchangeRateProperties exchangeRateProperties;
    
    @BeforeEach
    void setup() {
        cacheManager = new ConcurrentMapCacheManager();
        
        ReflectionTestUtils.setField(currencyListDataFetcherService, "cacheManager", cacheManager);
    }

    @Test
    public void getCurrencyList_shouldCallGetCurrencyList_whenInvoke(){
        Map<String, String> mockData = new HashMap<>();
        mockData.put("IDR", "Indonesian Rupiah");
        mockData.put("USD", "United States Dollar");

        when(exchangeRateWebClient.getCurrencyList()).thenReturn(mockData);
        when(exchangeRateProperties.getCacheName()).thenReturn("cn");

        currencyListDataFetcherService.fetchData(null);

        verify(exchangeRateWebClient, times(1)).getCurrencyList();
    }
}
