package com.interview.backend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

public class RateFormatterUtil {

    private static final int DEFAULT_SCALE = 8;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

    public static BigDecimal formatRate(Double rate) {
        return formatRate(rate, DEFAULT_SCALE);
    }

    public static BigDecimal formatRate(Double rate, int scale) {
        if (rate == null) {
            return null;
        }
        return BigDecimal.valueOf(rate).setScale(scale, DEFAULT_ROUNDING);
    }

    public static Map<String, BigDecimal> formatRatesMap(Map<String, Double> rates) {
        if (rates == null) {
            return null;
        }
        return rates.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> formatRate(entry.getValue())));
    }

    public static Map<String, Map<String, BigDecimal>> formatTimeSeriesRates(
            Map<String, Map<String, Double>> timeSeriesRates) {
        if (timeSeriesRates == null) {
            return null;
        }
        return timeSeriesRates.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> formatRatesMap(entry.getValue())));
    }
}
