package com.example.allo.client;

import com.example.allo.dto.HistoricalRatesResponse;
import com.example.allo.dto.LatestRatesResponse;

import java.util.Map;

public interface FrankfurterClient {

    LatestRatesResponse getLatestRates(String base);

    HistoricalRatesResponse getHistoricalRates(
            String start, String end, String from, String to);

    Map<String, String> getCurrencies();
}

