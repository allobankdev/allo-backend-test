package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.dto.LatestRatesResponse;
import com.mlutfiazizan13.allo_backend_test.exception.ExternalApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String GITHUB_USERNAME = "mlutfiazizan13";
    private static final BigDecimal SPREAD_FACTOR = calculateSpreadFactor(GITHUB_USERNAME);

    private final RestTemplate restTemplate;

    public LatestIdrRatesFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object fetchData() {
        try {
            LatestRatesResponse response = restTemplate.getForObject(
                    "/latest?base=IDR", LatestRatesResponse.class);
            if (response != null && response.getRates() != null) {
                applySpreadCalculation(response);
            }
            return response;
        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    "Failed to fetch latest IDR rates from Frankfurter API", ex);
        }
    }

    @Override
    public String getStrategyType() {
        return "latest_idr_rates";
    }

    private void applySpreadCalculation(LatestRatesResponse response) {
        BigDecimal rateUsd = response.getRates().get("USD");
        if (rateUsd != null && rateUsd.compareTo(BigDecimal.ZERO) != 0) {
            // USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
            BigDecimal inverseRate = BigDecimal.ONE.divide(rateUsd, MathContext.DECIMAL128);
            BigDecimal spreadMultiplier = BigDecimal.ONE.add(SPREAD_FACTOR);
            response.setUsdBuySpreadIdr(inverseRate.multiply(spreadMultiplier, MathContext.DECIMAL128));
        }
    }

    static BigDecimal calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return BigDecimal.valueOf(sum % 1000)
                .divide(BigDecimal.valueOf(100000), MathContext.DECIMAL128);
    }
}
