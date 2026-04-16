package com.allobank.finance.strategy;

import com.allobank.finance.exception.FinanceDataLoadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(WebClient webClient, @Value("${app.github-username:biascoder}") String githubUsername) {
        this.webClient = webClient;
        this.githubUsername = githubUsername.toLowerCase();
        this.spreadFactor = calculateSpreadFactor(this.githubUsername);
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> latestRates = Optional.ofNullable(response)
                .orElseThrow(() -> new FinanceDataLoadException("Frankfurter latest rates response was empty"));
        double rateUsd = readRate(castObjectMap(latestRates.get("rates"), "latest rates"), "USD");
        double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);

        Map<String, Object> result = new LinkedHashMap<>(latestRates);
        result.put("github_username", githubUsername);
        result.put("spread_factor", spreadFactor);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        return List.of(Map.copyOf(result));
    }

    static double calculateSpreadFactor(String githubUsername) {
        int sum = githubUsername.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castObjectMap(Object value, String sourceName) {
        if (!(value instanceof Map<?, ?>)) {
            throw new FinanceDataLoadException("Frankfurter " + sourceName + " response did not contain a rates object");
        }
        return (Map<String, Object>) value;
    }

    private double readRate(Map<String, Object> rates, String currency) {
        Object value = rates.get(currency);
        if (!(value instanceof Number number)) {
            throw new FinanceDataLoadException(currency + " rate was not present in latest rates response");
        }
        return number.doubleValue();
    }
}
