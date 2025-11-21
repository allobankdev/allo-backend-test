package com.example.allo_bank.service;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;

import java.math.BigDecimal;
import java.util.Map;

public interface GetIntegrationDataService {

    LatestIdrRatesDto getLatestIdrRates();
    HistoricalIdrUsdDto getHistoricalIdrUsd();
    Map<String, String> getSupportedCurrencies();

}
