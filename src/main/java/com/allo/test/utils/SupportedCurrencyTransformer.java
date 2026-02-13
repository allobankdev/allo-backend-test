package com.allo.test.utils;

import java.util.*;

public class SupportedCurrencyTransformer {

    public static List<Map<String, String>> transform(Map<?, ?> rawData) {

        List<Map<String, String>> unifiedResults = new ArrayList<>();

        if (rawData == null || rawData.isEmpty()) {
            return unifiedResults;
        }

        Map<String, String> sortedData = new TreeMap<>();

        for (Map.Entry<?, ?> entry : rawData.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            sortedData.put(key, value);
        }

        for (Map.Entry<String, String> entry : sortedData.entrySet()) {
            Map<String, String> currencyInfo = new LinkedHashMap<>();
            currencyInfo.put("code", entry.getKey());
            currencyInfo.put("name", entry.getValue());

            unifiedResults.add(currencyInfo);
        }

        return unifiedResults;
    }
}
