package com.self.bs.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.dto.response.LatestCurrencyRateResponseDto;
import com.self.bs.source.enumeration.CacheKeywordEnum;
import com.self.bs.source.webclient.ExchangeRateWebClient;

@Service
public class LatestCurrencyRateDataFetcherService implements IExchangeRateDataFetcher{

    @Autowired
    protected ConcurrentMapCacheManager cacheManager;

    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Autowired
    protected ExchangeRateWebClient exchangeRateWebClient;

    @Override
    public void fetchData(ExchangeRateDataFetcherRequestDto requestDto) {
        LatestCurrencyRateResponseDto data = exchangeRateWebClient.getLatestCurrencyRate(requestDto);
        
        // calculate USD_BuySpread_IDR
        int asci = exchangeRateProperties.getPersonalName().chars().sum() % 1000;
        Double spreadFactor = asci / 100000.0;

        Double calculate = (1 / Double.valueOf(data.getRates().get(requestDto.getTargetCurrency()))) * (1 + spreadFactor);

        data.setUSD_BuySpread_IDR(calculate);

        // save data
        cacheManager.getCache(exchangeRateProperties.getCacheName()).put(CacheKeywordEnum.LATEST_RATES.name(), data);
    }
}
