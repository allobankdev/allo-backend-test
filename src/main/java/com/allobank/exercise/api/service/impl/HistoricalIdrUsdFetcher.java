package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.ExchangeHistory;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.stereotype.Service;

@Service("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final ResourceCache resourceCache;

    public HistoricalIdrUsdFetcher(ResourceCache resourceCache) {
        this.resourceCache = resourceCache;
    }

    @Override
    public ExchangeHistory getData() {
        ExchangeHistoryResponse exchangeHistoryCache = resourceCache.getDataCache(ResourceType.HISTORICAL_IDR_USD);
        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.copyFrom(exchangeHistoryCache);

        return exchangeHistory;
    }
}
