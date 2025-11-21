package com.example.allo_bank.runner;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.GetIntegrationDataService;
import com.example.allo_bank.util.Cache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RunnerApp implements ApplicationRunner {

    private final GetIntegrationDataService getIntegrationDataService;
    private final Cache cache;

    public RunnerApp(GetIntegrationDataService getIntegrationDataService, Cache cache) {
        this.getIntegrationDataService = getIntegrationDataService;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        LatestIdrRatesDto latestRateResponse = getIntegrationDataService.getLatestIdrRates();
        HistoricalIdrUsdDto exchangeHistoryResponse = getIntegrationDataService.getHistoricalIdrUsd();
        Map<String, String> currencyResponse = getIntegrationDataService.getSupportedCurrencies();

        cache.initImmutableCache(latestRateResponse, exchangeHistoryResponse, currencyResponse);
    }
}
