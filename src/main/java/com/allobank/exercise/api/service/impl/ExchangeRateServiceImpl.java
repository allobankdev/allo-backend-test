package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.CurrencyInfo;
import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final FrankfurterClient frankfurterClient;

    public ExchangeRateServiceImpl(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public Object getLatestRates() {
        return null;
    }

    @Override
    public Object getHistoricalRates() {
        return null;
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        return frankfurterClient.getSupportedCurrencies();
    }
}
