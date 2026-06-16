package com.allobank.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class SpreadCalculator {

    /**
     * Calculates the spread factor based on GitHub username.
     * Formula: Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
     *
     * @param githubUsername The GitHub username (will be converted to lowercase)
     * @return The spread factor between 0.00000 and 0.00999
     */
    public static BigDecimal calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isEmpty()) {
            throw new IllegalArgumentException("GitHub username cannot be null or empty");
        }

        String lowerUsername = githubUsername.toLowerCase();
        int sumOfUnicode = lowerUsername.chars().sum();
        int modulo = sumOfUnicode % 1000;
        
        return BigDecimal.valueOf(modulo)
                .divide(BigDecimal.valueOf(100000.0), 5, RoundingMode.HALF_UP);
    }

    /**
     * Calculates USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     *
     * @param usdRate The USD rate from API when base=IDR
     * @param spreadFactor The spread factor calculated from GitHub username
     * @return The USD buy spread IDR value
     */
    public static BigDecimal calculateUsdBuySpreadIdr(BigDecimal usdRate, BigDecimal spreadFactor) {
        if (usdRate == null || usdRate.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("USD rate cannot be null or zero");
        }
        if (spreadFactor == null) {
            throw new IllegalArgumentException("Spread factor cannot be null");
        }

        BigDecimal inverseRate = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP);
        BigDecimal onePlusSpread = BigDecimal.ONE.add(spreadFactor);
        
        return inverseRate.multiply(onePlusSpread)
                .setScale(10, RoundingMode.HALF_UP);
    }
}

