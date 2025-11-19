package com.allobank.exercise.api.integration;

import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import java.util.Map;

public interface FrankfurterClient {

    Object getLatestRates();

    ExchangeHistoryResponse getExchangeHistory(String queryTime, String fromCurrency, String toCurrency);

    Map<String, String> getSupportedCurrencies();
}
