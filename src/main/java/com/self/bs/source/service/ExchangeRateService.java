package com.self.bs.source.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;
import com.self.bs.source.dto.response.ResponseDto;
import com.self.bs.source.enumeration.CacheKeywordEnum;
import com.self.bs.source.exception.ExchangeRateException;

@Service
public class ExchangeRateService {
    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Autowired
    protected ConcurrentMapCacheManager cacheManager;

    @Autowired
    protected IExchangeRateDataFetcher currencyListDataFetcherService;

    @Autowired
    protected IExchangeRateDataFetcher historyExchangeRateDataFetcherService;

    @Autowired
    protected IExchangeRateDataFetcher latestCurrencyRateDataFetcherService;

    public ResponseDto<Object> getCurrencyList(){
        try {
            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.CURRENCY_LIST.name()));
        } catch (NullPointerException e) {
            currencyListDataFetcherService.fetchData(null);

            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.CURRENCY_LIST.name()));
        }
    }

    public ResponseDto<Object> getExchangeRateHistorical(String dateFrom, String dateTo, String baseCurrency, String targetCurrency){
        String rangeDate = dateFrom.concat(exchangeRateProperties.getRangeDateSeparator()).concat(dateTo);
        try {
            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.HISTORICAL.name().concat(rangeDate)));
        } catch (NullPointerException e) {            
            if (LocalDate.parse(dateFrom).isAfter(LocalDate.parse(dateTo)))
                throw new ExchangeRateException(ExchangeRateException.DATE_FROM_CANNOT_BE_AFTER_DATE_TO);

            ExchangeRateDataFetcherRequestDto requestDto = new ExchangeRateDataFetcherRequestDto(dateFrom, dateTo, baseCurrency, targetCurrency);
            historyExchangeRateDataFetcherService.fetchData(requestDto);

            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.HISTORICAL.name().concat(rangeDate)));
        }
    }

    public ResponseDto<Object> getLatestExchangeRate(String baseCurrency, String targetCurrency){
        try {
            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.LATEST_RATES.name()));
        } catch (NullPointerException e) {
            ExchangeRateDataFetcherRequestDto requestDto = new ExchangeRateDataFetcherRequestDto();
            requestDto.setBaseCurrency(baseCurrency);
            requestDto.setTargetCurrency(targetCurrency);

            latestCurrencyRateDataFetcherService.fetchData(requestDto);

            return new ResponseDto<Object>(getDataFromMemory(CacheKeywordEnum.LATEST_RATES.name()));
        }
    }

    public Object getDataFromMemory(String keyword){
        return cacheManager.getCache(exchangeRateProperties.getCacheName()).get(keyword).get();
    }
}
