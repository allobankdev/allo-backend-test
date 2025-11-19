package com.allobank.exercise.api.boot;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.dto.CurrencyInfo;
import com.allobank.exercise.api.dto.ExchangeHistory;
import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.service.ExchangeRateService;
import com.allobank.exercise.api.service.impl.SupportedCurrencyFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements ApplicationRunner {

    private final ResourceCache resourceCache;
    private final ExchangeRateService exchangeRateService;

    public DataLoader
    (
        ResourceCache resourceCache,
        ExchangeRateService exchangeRateService
    )
    {
        this.resourceCache = resourceCache;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Map<String, String> currencyResponse = exchangeRateService.getSupportedCurrencies();
        ExchangeHistoryResponse exchangeHistoryResponse = exchangeRateService.getExchangeHistory("2024-01-01..2024-01-05?",
                "IDR", "USD");

        resourceCache.initImmutableCache(currencyResponse, exchangeHistoryResponse);
    }
}
