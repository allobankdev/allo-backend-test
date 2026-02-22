package com.allobank.finnance.allobankfinance.integration;

import com.allobank.finnance.allobankfinance.dto.frankfurter.HistoricalRatesResponse;
import com.allobank.finnance.allobankfinance.dto.frankfurter.LatestRatesResponse;

import java.util.Map;

public interface FrankfurterIntegrationServicev3 {

    LatestRatesResponse  getLatestUsdRates(String baseCurrency);
    HistoricalRatesResponse  getHistoricalRates(String startDate, String endDate, String baseCurrency, String to);
    Map<String, String> getSupportedCurrencies();
}
