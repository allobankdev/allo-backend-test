package com.allobank.exercise.api.integration;

import java.util.LinkedHashMap;

public interface FrankfurterClient {

    Object getLatestRates();

    Object getHistoricalRates();

    LinkedHashMap<String, String> getSupportedCurrencies();
}
