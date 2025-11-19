package com.allobank.exercise.api.cache;

import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceCache {

    private Map<String, Object> dataCache = new HashMap<>();

    public void initImmutableCache
    (
        ExchangeRateResponse latestRateResponse,
        Map<String, String> currencyResponse,
        ExchangeHistoryResponse exchangeHistoryResponse
    )
    {
        Map<String, Object> mutableDataCache = new HashMap<>();

        mutableDataCache.put(ResourceType.LATEST_IDR_RATES.getPath(), latestRateResponse);
        mutableDataCache.put(ResourceType.SUPPORTED_CURRENCIES.getPath(), currencyResponse);
        mutableDataCache.put(ResourceType.HISTORICAL_IDR_USD.getPath(), exchangeHistoryResponse);

        dataCache = Collections.unmodifiableMap(mutableDataCache);

    }

    @SuppressWarnings("unchecked")
    public <T> T getDataCache(ResourceType resourceType){
        Object data = dataCache.get(resourceType.getPath());
        return (T)data;
    }
}
