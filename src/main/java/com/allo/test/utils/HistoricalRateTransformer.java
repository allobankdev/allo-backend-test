package com.allo.test.utils;

import com.allo.test.dto.response.HistoricalIdrUsdResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class HistoricalRateTransformer {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);

    public static List<Map<String, Object>> transform(HistoricalIdrUsdResponse response) {
        List<Map<String, Object>> unifiedResults = new ArrayList<>();

        if (response == null || response.getRates() == null) {
            return unifiedResults;
        }

        Map<LocalDate, Map<String, BigDecimal>> sortedRates = new TreeMap<>(response.getRates());

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : sortedRates.entrySet()) {
            LocalDate date = entry.getKey();
            Map<String, BigDecimal> dailyRates = entry.getValue();

            BigDecimal usdRateRaw = dailyRates.get("USD");

            BigDecimal finalRate = BigDecimal.ZERO;

            if (usdRateRaw != null && usdRateRaw.compareTo(BigDecimal.ZERO) > 0) {
                finalRate = BigDecimal.ONE.divide(usdRateRaw, MC)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            Map<String, Object> dataContent = new LinkedHashMap<>();
            dataContent.put("date", date.toString());
            dataContent.put("rate", finalRate);

            unifiedResults.add(dataContent);
        }

        return unifiedResults;
    }
}