package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public HistoricalIdrUsdFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<Map<String, Object>> fetch() {
        Map<String, Object> rawResponse = frankfurterClient.fetchHistoricalIdrUsdRaw();

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> ratesByDate = (Map<String, Map<String, Object>>) rawResponse.getOrDefault("rates",
                Map.of());

        return ratesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    BigDecimal usdPerIdr = toBigDecimal(entry.getValue().get("USD"));

                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("resourceType", resourceType());
                    result.put("date", entry.getKey());
                    result.put("usd_per_idr", usdPerIdr);
                    return result;
                })
                .toList();
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("USD rate is missing from historical upstream response.");
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        if (value instanceof String stringValue) {
            return new BigDecimal(stringValue);
        }
        throw new IllegalArgumentException("Numeric value expected but got: " + value);
    }
}
