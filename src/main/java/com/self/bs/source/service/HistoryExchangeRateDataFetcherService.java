package com.self.bs.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.dto.response.HistoryCurrencyRateResponseDto;
import com.self.bs.source.enumeration.CacheKeywordEnum;
import com.self.bs.source.webclient.ExchangeRateWebClient;

@Service
public class HistoryExchangeRateDataFetcherService implements IExchangeRateDataFetcher{

    @Autowired
    protected ConcurrentMapCacheManager cacheManager;

    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Autowired
    protected ExchangeRateWebClient exchangeRateWebClient;

    @Override
    public void fetchData(ExchangeRateDataFetcherRequestDto requestDto) {
        String rangeDate = requestDto.getDateFrom().concat(exchangeRateProperties.getRangeDateSeparator()).concat(requestDto.getDateTo());

        HistoryCurrencyRateResponseDto data = exchangeRateWebClient.getHistoryCurrencyRate(requestDto, rangeDate);

        cacheManager.getCache(exchangeRateProperties.getCacheName()).put(CacheKeywordEnum.HISTORICAL.name().concat(rangeDate), data);
    }
}
