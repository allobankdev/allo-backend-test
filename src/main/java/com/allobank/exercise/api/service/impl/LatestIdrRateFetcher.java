package com.allobank.exercise.api.service.impl;


import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.ExchangeRate;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import com.allobank.exercise.api.service.IDRDataFetcher;
import com.allobank.exercise.api.util.CalculatorFinance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("latest_idr_rates")
public class LatestIdrRateFetcher implements IDRDataFetcher {

    private final CalculatorFinance calculatorFinance;
    private final ResourceCache resourceCache;

    public LatestIdrRateFetcher(CalculatorFinance calculatorFinance, ResourceCache resourceCache) {
        this.calculatorFinance = calculatorFinance;
        this.resourceCache = resourceCache;
    }

    @Override
    public ExchangeRate getData() {
        ExchangeRateResponse exchangeRateCache = resourceCache.getDataCache(ResourceType.LATEST_IDR_RATES);
        BigDecimal usdRate = exchangeRateCache.getRates().get("USD");
        BigDecimal USDBuySpreadIDR = calculatorFinance.calculateUSDBuySpreadIDR(usdRate);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.copyFrom(exchangeRateCache, USDBuySpreadIDR);

        return exchangeRate;
    }
}
