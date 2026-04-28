package com.allobank.finance.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private static final Logger log = Logger.getLogger(LatestIDRRatesStrategy.class.getName());
    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final WebClient webClient;
    private final double spreadFactor;

    public LatestIDRRatesStrategy(
            WebClient webClient,
            @Value("${spread.github-username}") String githubUsername) {
        this.webClient = webClient;
        this.spreadFactor = calculateSpreadFactor(githubUsername);
        log.info(String.format("[LatestIDRRatesStrategy] Username: '%s', Spread Factor: %.5f",
                githubUsername, this.spreadFactor));
    }

    @Override
    public List<Map<String, Object>> fetch() {
        log.info("[LatestIDRRatesStrategy] Fetching latest IDR rates...");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("Response null dari Frankfurter API");
            }

            return transformResponse(response);

        } catch (WebClientResponseException ex) {
            log.severe("[LatestIDRRatesStrategy] HTTP error: " + ex.getStatusCode());
            throw new RuntimeException("Gagal fetch latest IDR rates: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.severe("[LatestIDRRatesStrategy] Error: " + ex.getMessage());
            throw new RuntimeException("Gagal fetch latest IDR rates", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> transformResponse(Map<String, Object> response) {
        Map<String, Object> rates = (Map<String, Object>) response.get("rates");
        List<Map<String, Object>> results = new ArrayList<>();

        if (rates == null) {
            log.warning("[LatestIDRRatesStrategy] Field 'rates' null");
            return results;
        }

        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("base", response.get("base"));
        entry.put("date", response.get("date"));
        entry.put("rates", rates);

        Object usdRateObj = rates.get("USD");
        if (usdRateObj != null) {
            double rateUSD = ((Number) usdRateObj).doubleValue();
            double usdBuySpreadIDR = calculateUsdBuySpreadIDR(rateUSD);
            entry.put("USD_BuySpread_IDR", BigDecimal.valueOf(usdBuySpreadIDR)
                    .setScale(8, RoundingMode.HALF_UP)
                    .doubleValue());
            entry.put("spreadFactorApplied", BigDecimal.valueOf(spreadFactor)
                    .setScale(5, RoundingMode.HALF_UP)
                    .doubleValue());
        }

        results.add(entry);
        return results;
    }

    double calculateUsdBuySpreadIDR(double rateUSD) {
        if (rateUSD == 0) {
            throw new ArithmeticException("Rate_USD tidak boleh nol");
        }
        return (1.0 / rateUSD) * (1.0 + spreadFactor);
    }

    static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("GitHub username tidak boleh kosong");
        }
        int unicodeSum = githubUsername.toLowerCase()
                .chars()
                .sum();
        return (unicodeSum % 1000) / 100000.0;
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }
}