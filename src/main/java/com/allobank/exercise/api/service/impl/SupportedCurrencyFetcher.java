package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.CurrencyInfo;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("supported_currencies")
public class SupportedCurrencyFetcher implements IDRDataFetcher {

    private final ResourceCache resourceCache;

    public SupportedCurrencyFetcher(ResourceCache resourceCache) {
        this.resourceCache = resourceCache;
    }

    @Override
    public List<CurrencyInfo> getData() {
        Map<String, String> currencyCache = resourceCache.getDataCache(ResourceType.SUPPORTED_CURRENCIES);

        return currencyCache.entrySet().stream()
                .map(e -> new CurrencyInfo(e.getKey(), e.getValue()))
                .toList();
    }
}
