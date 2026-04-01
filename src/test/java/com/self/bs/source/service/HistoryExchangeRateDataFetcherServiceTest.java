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
import com.self.bs.source.dto.response.HistoryCurrencyRateResponseDto;
import com.self.bs.source.webclient.ExchangeRateWebClient;

@ExtendWith(MockitoExtension.class)
public class HistoryExchangeRateDataFetcherServiceTest {
    @InjectMocks
    protected HistoryExchangeRateDataFetcherService historyExchangeRateDataFetcherService;

    @Mock
    protected ExchangeRateWebClient exchangeRateWebClient;

    @Mock
    protected ConcurrentMapCacheManager cacheManager;

    @Mock
    protected ExchangeRateProperties exchangeRateProperties;
    
    @BeforeEach
    void setup() {
        cacheManager = new ConcurrentMapCacheManager();
        
        ReflectionTestUtils.setField(historyExchangeRateDataFetcherService, "cacheManager", cacheManager);
    }

    @Test
    public void getHistoryExchangeRate_shouldCallHistoryCurrencyRate_whenInvoke(){
        ExchangeRateDataFetcherRequestDto requestDto = new ExchangeRateDataFetcherRequestDto("2026-03-31", "2026-04-01", "IDR", "USD");
        String rangeDate = requestDto.getDateFrom().concat("..").concat(requestDto.getDateTo());

        Map<String, Map<String, String>> rates = new HashMap<>();
        rates.put(requestDto.getDateFrom(), Map.of("USD", "0.005"));
        rates.put(requestDto.getDateTo(), Map.of("USD", "0.006"));

        HistoryCurrencyRateResponseDto mockData = new HistoryCurrencyRateResponseDto("1.5", requestDto.getBaseCurrency(), requestDto.getDateFrom(), requestDto.getDateTo(), rates);

        when(exchangeRateWebClient.getHistoryCurrencyRate(requestDto, rangeDate)).thenReturn(mockData);
        when(exchangeRateProperties.getCacheName()).thenReturn("cn");
        when(exchangeRateProperties.getRangeDateSeparator()).thenReturn("..");

        historyExchangeRateDataFetcherService.fetchData(requestDto);

        verify(exchangeRateWebClient, times(1)).getHistoryCurrencyRate(requestDto, rangeDate);
    }
}
