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
import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.dto.response.LatestCurrencyRateResponseDto;
import com.self.bs.source.webclient.ExchangeRateWebClient;

@ExtendWith(MockitoExtension.class)
public class LatestCurrencyRateDataFetcherServiceTest {
    @InjectMocks
    protected LatestCurrencyRateDataFetcherService latestCurrencyRateDataFetcherService;

    @Mock
    protected ExchangeRateWebClient exchangeRateWebClient;

    @Mock
    protected ConcurrentMapCacheManager cacheManager;

    @Mock
    protected ExchangeRateProperties exchangeRateProperties;
    
    @BeforeEach
    void setup() {
        cacheManager = new ConcurrentMapCacheManager();
        
        ReflectionTestUtils.setField(latestCurrencyRateDataFetcherService, "cacheManager", cacheManager);
    }

    @Test
    public void getLatestCurrencyRate_shouldCallGetLatestCurrencyRate_whenInvoke(){
        ExchangeRateDataFetcherRequestDto requestDto = new ExchangeRateDataFetcherRequestDto("2026-03-31", "2026-04-01", "IDR", "USD");
        
        Map<String, String> rates = new HashMap<>();
        rates.put("USD", "0.005");

        LatestCurrencyRateResponseDto mockData = new LatestCurrencyRateResponseDto("1.5", requestDto.getBaseCurrency(), "2026-04-01", rates, 15.0);
        when(exchangeRateWebClient.getLatestCurrencyRate(requestDto)).thenReturn(mockData);
        when(exchangeRateProperties.getCacheName()).thenReturn("cn");
        when(exchangeRateProperties.getPersonalName()).thenReturn("bobbisetiawan");

        latestCurrencyRateDataFetcherService.fetchData(requestDto);

        verify(exchangeRateWebClient, times(1)).getLatestCurrencyRate(requestDto);
    }
}
