package com.example.allo_bank.service.impl;

import com.example.allo_bank.integration.FrankfurterFetcher;
import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.GetIntegrationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetIntegrationDataServiceImpl implements GetIntegrationDataService {

    @Autowired
    private FrankfurterFetcher frankfurterFetcher;

    private static final Logger log = LoggerFactory.getLogger(GetIntegrationDataServiceImpl.class);

    @Override
    public LatestIdrRatesDto getLatestIdrRates() {
        return frankfurterFetcher.getLatestIdrRates();
    }

    @Override
    public HistoricalIdrUsdDto getHistoricalIdrUsd() {
        return frankfurterFetcher.getHistoricalIdrUsd();
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        return frankfurterFetcher.getSupportedCurrencies();
    }
}
