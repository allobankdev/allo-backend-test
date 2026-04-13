package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import com.allobank.test.config.FrankfurterApiProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final MathContext MATH_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);
    private static final BigDecimal ONE = BigDecimal.ONE;

    private final FrankfurterClient frankfurterClient;
    private final FrankfurterApiProperties properties;

    public LatestIdrRatesFetcher(FrankfurterClient frankfurterClient, FrankfurterApiProperties properties) {
        this.frankfurterClient = frankfurterClient;
        this.properties = properties;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Map<String, Object>> fetch() {
        Map<String, Object> rawResponse = frankfurterClient.fetchLatestIdrRatesRaw();

        @SuppressWarnings("unchecked")
        Map<String, Object> rates = (Map<String, Object>) rawResponse.getOrDefault("rates", Map.of());

        BigDecimal rateUsd = toBigDecimal(rates.get("USD"));
        if (rateUsd.signum() <= 0) {
            throw new IllegalArgumentException("Rate USD must be positive.");
        }

        BigDecimal spreadFactor = calculateSpreadFactor(properties.getGithubUsername());
        BigDecimal usdBuySpreadIdr = ONE.divide(rateUsd, MATH_CONTEXT)
                .multiply(ONE.add(spreadFactor), MATH_CONTEXT)
                .setScale(8, RoundingMode.HALF_UP);

        Map<String, Object> enrichedRates = new LinkedHashMap<>(rates);
        enrichedRates.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resourceType", resourceType());
        result.put("base", rawResponse.getOrDefault("base", "IDR"));
        result.put("date", rawResponse.get("date"));
        result.put("spread_factor", spreadFactor.setScale(5, RoundingMode.HALF_UP));
        result.put("rates", enrichedRates);
        return List.of(result);
    }

    private static BigDecimal calculateSpreadFactor(String githubUsername) {
        int asciiSum = githubUsername == null
                ? 0
                : githubUsername.toLowerCase(Locale.ROOT).chars().sum();
        int modValue = asciiSum % 1000;
        return BigDecimal.valueOf(modValue)
                .divide(BigDecimal.valueOf(100000L), MATH_CONTEXT);
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("USD rate is missing from upstream response.");
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString(), MATH_CONTEXT);
        }
        if (value instanceof String stringValue) {
            return new BigDecimal(stringValue, MATH_CONTEXT);
        }
        throw new IllegalArgumentException("Numeric value expected but got: " + value);
    }
}
