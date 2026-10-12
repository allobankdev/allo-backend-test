package com.allobank.strategy;

import com.allobank.dto.LatestRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Strategy for fetching latest IDR rates and applying custom spread factor.
 * The spread factor is calculated based on the sum of Unicode values of
 * the GitHub username.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesStrategy implements IDRDataFetcher {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;
    
    @Value("${github.username}")
    private String githubUsername;
    
    private static final String RESOURCE_TYPE = "latest_idr_rates";
    
    @Override
    public Object fetchData() throws Exception {
        log.info("Fetching latest IDR rates from Frankfurter API");
        
        String url = baseUrl + "/latest?base=IDR";
        String response = restTemplate.getForObject(url, String.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch latest rates: null response");
        }
        
        Map<String, Object> data = objectMapper.readValue(response, Map.class);
        
        // Extract rates
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> rates = (Map<String, BigDecimal>) data.get("rates");
        
        // Build response with spread factor
        LatestRatesResponse ratesResponse = LatestRatesResponse.builder()
                .base((String) data.get("base"))
                .date((String) data.get("date"))
                .rates(rates)
                .usdBuySpreadIDR(calculateUSDSpreadRate(rates))
                .build();
        
        log.info("Successfully fetched and transformed latest IDR rates");
        return ratesResponse;
    }
    
    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
    
    /**
     * Calculate the USD buy spread in IDR.
     * Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     * where Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
     */
    private BigDecimal calculateUSDSpreadRate(Map<String, BigDecimal> rates) {
        BigDecimal usdRate = rates.get("USD");
        if (usdRate == null || usdRate.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("USD rate not found or is zero");
        }
        
        double spreadFactor = calculateSpreadFactor();
        log.debug("Calculated spread factor: {}", spreadFactor);
        
        BigDecimal inverseCrate = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP);
        BigDecimal spreadMultiplier = BigDecimal.valueOf(1.0 + spreadFactor);
        BigDecimal result = inverseCrate.multiply(spreadMultiplier);
        
        return result.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate spread factor from GitHub username.
     * Steps:
     * 1. Sum Unicode values of all lowercase characters in username
     * 2. Take modulo 1000
     * 3. Divide by 100000.0
     */
    private double calculateSpreadFactor() {
        String lowercase = githubUsername.toLowerCase();
        long unicodeSum = 0;
        
        for (char c : lowercase.toCharArray()) {
            unicodeSum += (int) c;
        }
        
        double factor = (unicodeSum % 1000) / 100000.0;
        log.info("GitHub username: {}, Unicode sum: {}, Spread factor: {}", 
                githubUsername, unicodeSum, factor);
        
        return factor;
    }
}
