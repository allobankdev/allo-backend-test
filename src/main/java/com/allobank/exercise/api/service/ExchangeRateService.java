package com.allobank.exercise.api.service;

import com.allobank.exercise.api.dto.CurrencyInfo;

import java.util.List;
import java.util.Map;

public interface ExchangeRateService {
    Object getLatestRates();
    Object getHistoricalRates();
    Map<String, String> getSupportedCurrencies();
}
