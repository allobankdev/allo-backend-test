package com.allobank.exercise.api.service;

import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;

import java.util.Map;

public interface ExchangeRateService {
    ExchangeRateResponse getLatestRates();
    ExchangeHistoryResponse getExchangeHistory(String queryTime, String fromCurrency, String toCurrency);
    Map<String, String> getSupportedCurrencies();
}
