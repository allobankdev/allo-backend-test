package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.config.FrankfurterApiProperties;
import com.allobank.assignment.exception.ExternalServiceException;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.LatestRatesAggregation;
import com.allobank.assignment.model.LatestRatesResponse;
import com.allobank.assignment.model.ResourceType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;

@Component
public class LatestIdrRatesStrategy implements IdrDataFetchStrategy{

    private static final String USD_CODE = "USD";
    private final FrankfurterApiClient apiClient;
    private final FrankfurterApiProperties properties;

    public LatestIdrRatesStrategy(FrankfurterApiClient apiClient, FrankfurterApiProperties properties) {
        this.apiClient = apiClient;
        this.properties = properties;
    }

    @Override
    public ResourceType supports() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public FinanceDataResponse fetch() {
        LatestRatesResponse response = apiClient.getLatestRates("IDR");
        Map<String, BigDecimal> rates = response.rates();
        if (rates == null || !rates.containsKey(USD_CODE)) {
            throw new ExternalServiceException("Frankfurter latest rates response missing USD rate");
        }

        BigDecimal usdRate = rates.get(USD_CODE);
        if (BigDecimal.ZERO.compareTo(usdRate) == 0) {
            throw new ExternalServiceException("Frankfurter latest rates USD value is zero");
        }
        BigDecimal spreadFactor = calculateSpreadFactor(properties.getGithubUsername());
        BigDecimal usdBuySpreadIdr = BigDecimal.ONE.divide(usdRate, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(6, RoundingMode.HALF_UP);

        LatestRatesAggregation payload = new LatestRatesAggregation(
                response.amount(),
                response.base(),
                response.date(),
                rates,
                usdBuySpreadIdr);

        return new FinanceDataResponse(supports().value(), payload, Instant.now());
    }

    private BigDecimal calculateSpreadFactor(String githubUsername) {
        String username = githubUsername == null ? "" : githubUsername.toLowerCase();
        int sum = username.chars().sum();
        BigDecimal mod = BigDecimal.valueOf(sum % 1000);
        return mod.divide(BigDecimal.valueOf(100_000), 5, RoundingMode.HALF_UP);
    }
}
