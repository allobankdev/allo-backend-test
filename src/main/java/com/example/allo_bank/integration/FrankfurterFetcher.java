package com.example.allo_bank.integration;

import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;

import java.util.Map;

public interface FrankfurterFetcher {

    LatestIdrRatesDto getLatestIdrRates();

    HistoricalIdrUsdDto getHistoricalIdrUsd();

    Map<String, String> getSupportedCurrencies();

}
