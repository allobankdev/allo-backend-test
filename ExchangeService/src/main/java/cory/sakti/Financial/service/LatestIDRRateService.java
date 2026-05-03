package cory.sakti.Financial.service;

import cory.sakti.Financial.dto.IDRRateData;
import cory.sakti.Financial.strategy.AbstractFinancialStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service("latest_idr_rates")
public class LatestIDRRateService extends AbstractFinancialStrategy {

    private final String githubUsername;

    public LatestIDRRateService(@Value("${app.github.username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() { return "latest_idr_rates"; }

    @Override
    protected String getUri() { return "/latest?base=IDR"; }

    @Override
    protected Object transform(JsonNode node) {
        String base = node.path("base").asText();
        String date = node.path("date").asText();
        BigDecimal usdRate = new BigDecimal(node.path("rates").path("USD").asText("0"));

        // 2. Perform Calculations
        BigDecimal factor = calculateSpreadFactor(this.githubUsername);
        BigDecimal buySpread = calculateBuySpread(usdRate, factor);

        // 3. Extract and Seal the Rates Map (Constraint C)
        Map<String, BigDecimal> rates = new HashMap<>();
        node.path("rates").fields().forEachRemaining(entry ->
                rates.put(entry.getKey(), new BigDecimal(entry.getValue().asText()))
        );

        // 4. Return the Immutable Record
        return new IDRRateData(
                base,
                date,
                Map.copyOf(rates), // Deep immutability for the map
                buySpread,
                factor
        );
    }

    public BigDecimal calculateSpreadFactor(String username) {
        if (username == null || username.isBlank()) return BigDecimal.ZERO;
        int asciiSum = username.chars().sum();
        double factor = (asciiSum % 1000) / 100000.0;
        return BigDecimal.valueOf(factor).stripTrailingZeros();
    }

    /**
     * ATOMIC GREEN: Implements (1 / rate) * (1 + factor)
     */
    public BigDecimal calculateBuySpread(BigDecimal usdRate, BigDecimal factor) {
        if (usdRate.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        // Use high precision for division to avoid ArithmeticException
        return BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(factor))
                .stripTrailingZeros();
    }
}
