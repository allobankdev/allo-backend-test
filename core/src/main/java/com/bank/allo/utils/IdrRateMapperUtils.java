package com.bank.allo.utils;

import java.util.Map;

import com.bank.allo.domain.idr.HistoricalRates;
import com.bank.allo.domain.idr.LatestRates;
import com.bank.allo.domain.idr.SupportedCurrencies;

public final class IdrRateMapperUtils {

    private IdrRateMapperUtils() {}

    // -------------------------------
    // LATEST IDR RATES MAPPER
    // -------------------------------
    public static LatestRates toLatestRates(Map<String, Object> resp, String githubUsername) {

        String base = (String) resp.getOrDefault("base", "IDR");
        String date = (String) resp.getOrDefault("date", "");
        Map<String, Double> rates = (Map<String, Double>) resp.getOrDefault("rates", Map.of());

        Double rateUsd = rates.get("USD");

        double spreadFactor = SpreadCalculator.calculateSpreadFactor(githubUsername);

        Double usdBuySpreadIdr = null;
        if (rateUsd != null) {
            usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);
        }

        return LatestRates.builder()
                .base(base)
                .date(date)
                .rates(rates)
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .spreadFactor(spreadFactor)
                .build();
    }

    // -------------------------------
    // HISTORICAL RATES MAPPER
    // -------------------------------
    public static HistoricalRates toHistoricalRates(Map<String, Object> resp) {
        String start = (String) resp.getOrDefault("start_date", "2024-01-01");
        String end = (String) resp.getOrDefault("end_date", "2024-01-05");

        Map<String, Map<String, Double>> rates =
                (Map<String, Map<String, Double>>) resp.getOrDefault("rates", Map.of());

        return HistoricalRates.builder()
                .startDate(start)
                .endDate(end)
                .rates(rates)
                .build();
    }

    // -------------------------------
    // SUPPORTED CURRENCIES MAPPER
    // -------------------------------
    public static SupportedCurrencies toSupportedCurrencies(Map<String, String> currencies) {
        return SupportedCurrencies.builder()
                .currencies(currencies)
                .build();
    }
}
