package com.example.allobank.strategy;

import com.example.allobank.dto.LatestIDRResponse;
import com.example.allobank.dto.HistoricalRatesResponse;

import java.util.Map;

public interface FrankfurterService {
    LatestIDRResponse getLatestBaseIDR(String url);
    HistoricalRatesResponse getHistoricalRatesDate(String url);
    Map<String,String> getCurrencies(String url);
}
