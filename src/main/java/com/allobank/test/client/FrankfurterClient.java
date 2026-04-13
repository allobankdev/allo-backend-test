package com.allobank.test.client;

import com.allobank.test.config.FrankfurterApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FrankfurterClient {

    private static final MathContext MATH_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);
    private static final BigDecimal ONE = BigDecimal.ONE;

    private final RestTemplate restTemplate;
    private final FrankfurterApiProperties properties;

    public FrankfurterClient(RestTemplate restTemplate, FrankfurterApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Map<String, Object> fetchLatestIdrRates() {
        String url = properties.getBaseUrl() + "/latest?from=EUR&to=IDR,USD";
        Map<String, Object> response = getMap(url);

        @SuppressWarnings("unchecked")
        Map<String, Object> rates = (Map<String, Object>) response.getOrDefault("rates", Map.of());

        BigDecimal rateUsd = toBigDecimal(rates.get("USD"));
        BigDecimal usdBuySpreadIdr = ONE.divide(rateUsd, MATH_CONTEXT)
                .multiply(ONE.add(calculateSpreadFactor(properties.getGithubUsername())), MATH_CONTEXT);

        Map<String, Object> enrichedRates = new LinkedHashMap<>(rates);
        enrichedRates.put("USD_BuySpread_IDR", usdBuySpreadIdr.setScale(8, RoundingMode.HALF_UP));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("base", response.get("base"));
        result.put("EUR", 1);
        result.put("date", response.get("date"));
        result.put("rates", enrichedRates);
        return result;
    }

    public Map<String, Object> fetchHistoricalIdrUsd() {
        String url = properties.getBaseUrl() + "/" + properties.getHistoricalRange() + "?from=USD&to=IDR";
        Map<String, Object> response = getMap(url);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> ratesByDate = (Map<String, Map<String, Object>>) response.getOrDefault("rates",
                Map.of());

        List<Map<String, Object>> history = ratesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    BigDecimal idrPerUsd = toBigDecimal(entry.getValue().get("IDR"));
                    return Map.<String, Object>of(
                            "date", entry.getKey(),
                            "idr_per_usd", idrPerUsd);
                })
                .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("base", "USD");
        result.put("USD", 1);
        result.put("quote", "IDR");
        result.put("rates", history);
        return result;
    }

    public Map<String, String> fetchSupportedCurrencies() {
        String url = properties.getBaseUrl() + "/currencies";
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody() == null ? Map.of() : response.getBody();
    }

    private Map<String, Object> getMap(String url) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody() == null ? Map.of() : response.getBody();
    }

    private static BigDecimal calculateSpreadFactor(String githubUsername) {
        int asciiSum = githubUsername == null
                ? 0
                : githubUsername.chars().sum();
        int modValue = asciiSum % 1000;
        return BigDecimal.valueOf(modValue)
                .divide(BigDecimal.valueOf(100000L), MATH_CONTEXT);
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        if (value instanceof String stringValue) {
            return new BigDecimal(stringValue, MATH_CONTEXT);
        }
        throw new IllegalArgumentException("Numeric value expected but got: " + value);
    }
}
