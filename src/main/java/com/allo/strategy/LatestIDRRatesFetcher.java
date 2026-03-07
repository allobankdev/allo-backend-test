package com.allo.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.allo.dto.FinanceResourceResponse;
import com.allo.exception.ExternalApiException;

@Component("latest_idr_rates")
public class LatestIDRRatesFetcher implements IDRFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestIDRRatesFetcher.class);

    static final String GITHUB_USERNAME = "yoelngl";
    static final double SPREAD_FACTOR = calculateSpreadFactor(GITHUB_USERNAME);

    private final RestTemplate restTemplate;

    public LatestIDRRatesFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<FinanceResourceResponse> fetch() {
        log.info("Fetching latest IDR rates from Frankfurter API");
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "/latest?base=IDR",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("rates")) {
                throw new ExternalApiException("Invalid response from Frankfurter API: missing 'rates'");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> rates = (Map<String, Object>) body.get("rates");

            Object usdRateObj = rates.get("USD");
            if (usdRateObj == null) {
                throw new ExternalApiException("USD rate not found in latest IDR rates response");
            }
            BigDecimal usdRate = (usdRateObj instanceof BigDecimal)
                    ? (BigDecimal) usdRateObj
                    : BigDecimal.valueOf(((Number) usdRateObj).doubleValue());
            BigDecimal spreadFactor = BigDecimal.valueOf(SPREAD_FACTOR);
            BigDecimal usdBuySpreadIDR = BigDecimal.ONE
                    .divide(usdRate, 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.ONE.add(spreadFactor))
                    .setScale(2, RoundingMode.HALF_UP);

            Map<String, Object> enrichedData = new LinkedHashMap<>(body);
            enrichedData.put("USD_BuySpread_IDR", usdBuySpreadIDR);
            enrichedData.put("spread_factor", spreadFactor);
            enrichedData.put("github_username", GITHUB_USERNAME);

            return List.of(new FinanceResourceResponse(resourceType(), enrichedData));

        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to fetch latest IDR rates: " + ex.getMessage(), ex);
        }
    }

    static double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100_000.0;
    }
}
