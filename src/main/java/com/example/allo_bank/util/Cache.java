package com.example.allo_bank.util;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class Cache {

    Logger log = LoggerFactory.getLogger(Cache.class);

    private Map<String, Object> dataCache = new HashMap<>();
    private boolean isReady = false;

    public synchronized void initImmutableCache
            (
                    LatestIdrRatesDto latestIdrRatesDto,
                    HistoricalIdrUsdDto historicalIdrUsdDto,
                    Map<String, String> currencyResponse
            )
    {
        Map<String, Object> mutableDataCache = new HashMap<>();

        mutableDataCache.put(TypeEnum.latest_idr_rates.getPath(), latestIdrRatesDto);
        mutableDataCache.put(TypeEnum.historical_idr_usd.getPath(), historicalIdrUsdDto);
        mutableDataCache.put(TypeEnum.supported_currencies.getPath(), currencyResponse);

        dataCache = Collections.unmodifiableMap(mutableDataCache);

        isReady = true;

    }

    @SuppressWarnings("unchecked")
    public <T> T getDataCache(TypeEnum resourceType){
        Object data = dataCache.get(resourceType.getPath());
        return (T)data;
    }

    public Map <String, Object> getAllCache(){
        return dataCache;
    }

    public boolean isReady() {
        return isReady;
    }

}
