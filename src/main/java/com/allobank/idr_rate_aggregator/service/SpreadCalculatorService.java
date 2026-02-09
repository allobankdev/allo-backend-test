package com.allobank.idr_rate_aggregator.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service untuk menghitung spread factor berdasarkan GitHub username
 * dan menghitung USD_BuySpread_IDR
 */
@Slf4j
@Service
public class SpreadCalculatorService {

    @Value("${app.github-username}")
    private String githubUsername;

    /**
     * Hitung spread factor dari GitHub username
     * 
     * Formula:
     * 1. Ubah username ke lowercase
     * 2. Hitung sum dari nilai Unicode setiap karakter
     * 3. Spread Factor = (Sum % 1000) / 100000.0
     * 
     * @return spread factor (nilai antara 0.00000 - 0.00999)
     */
    public BigDecimal calculateSpreadFactor() {
        String lowercaseUsername = githubUsername.toLowerCase();
        
        // Hitung sum Unicode values
        int unicodeSum = 0;
        for (char c : lowercaseUsername.toCharArray()) {
            unicodeSum += (int) c;
        }
        
        log.debug("GitHub username: {}", githubUsername);
        log.debug("Lowercase: {}", lowercaseUsername);
        log.debug("Unicode sum: {}", unicodeSum);
        
        // Hitung spread factor: (sum % 1000) / 100000.0
        int modResult = unicodeSum % 1000;
        BigDecimal spreadFactor = BigDecimal.valueOf(modResult)
                .divide(BigDecimal.valueOf(100000.0), 10, RoundingMode.HALF_UP);
        
        log.info("Spread Factor yang dihitung: {}", spreadFactor);
        
        return spreadFactor;
    }

    /**
     * Hitung USD_BuySpread_IDR
     * 
     * Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     * 
     * @param rateUSD rate USD dari API (dalam base IDR)
     * @return USD_BuySpread_IDR
     */
    public BigDecimal calculateUsdBuySpread(BigDecimal rateUSD) {
        BigDecimal spreadFactor = calculateSpreadFactor();
        
        // (1 / Rate_USD)
        BigDecimal inverseRate = BigDecimal.ONE.divide(rateUSD, 10, RoundingMode.HALF_UP);
        
        // (1 + Spread Factor)
        BigDecimal onePlusSpread = BigDecimal.ONE.add(spreadFactor);
        
        // Final: inverseRate * onePlusSpread
        BigDecimal usdBuySpread = inverseRate.multiply(onePlusSpread)
                .setScale(6, RoundingMode.HALF_UP);
        
        log.debug("Rate USD: {}, USD_BuySpread_IDR: {}", rateUSD, usdBuySpread);
        
        return usdBuySpread;
    }
}