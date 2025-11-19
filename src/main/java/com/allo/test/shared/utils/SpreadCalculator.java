package com.allo.test.shared.utils;

import com.allo.test.modules.finance.exceptions.MissingRequiredConfigException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for calculating spread factors and USD buy spread for IDR rates.
 * The spread factor is personalized based on a GitHub username.
 */
@Slf4j
@UtilityClass
public final class SpreadCalculator {

    /**
     * Calculates a unique spread factor based on a GitHub username.
     * <p>
     * Formula: (Sum of Unicode Values % 1000) / 100000.0
     * <p>
     * This yields a factor between 0.00000 and 0.00999.
     *
     * @param githubUsername the GitHub username (will be converted to lowercase)
     * @return the calculated spread factor
     */
    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.trim().isEmpty()) {
            log.error("GitHub username is null or empty - required configuration missing");
            throw new MissingRequiredConfigException();
        }

        String lowercase = githubUsername.toLowerCase();
        int sum = 0;

        for (char c : lowercase.toCharArray()) {
            sum += c;
        }

        double spreadFactor = ((sum % 1000) / 100000.0);

        log.info("Spread factor calculated for username '{}': sum={}, factor={}",
                githubUsername, sum, spreadFactor);

        return spreadFactor;
    }

    /**
     * Calculates the USD buy spread in IDR after applying the banking spread/margin.
     * <p>
     * Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     * <p>
     * where Rate_USD is the exchange rate from the API when base=IDR
     * (i.e., how many USD per 1 IDR).
     *
     * @param usdRateFromIDR the USD exchange rate from the API (IDR to USD)
     * @param spreadFactor   the spread factor to apply
     * @return the USD buy spread in IDR, rounded to 2 decimal places
     */
    public static BigDecimal calculateUsdBuySpread(BigDecimal usdRateFromIDR, double spreadFactor) {
        if (usdRateFromIDR == null || usdRateFromIDR.compareTo(BigDecimal.ZERO) == 0) {
            log.error("USD rate is null or zero, cannot calculate spread");
            return BigDecimal.ZERO;
        }

        // Convert spread factor to BigDecimal
        BigDecimal spreadFactorBD = BigDecimal.valueOf(spreadFactor);

        // Calculate: (1 / Rate_USD) * (1 + Spread Factor)
        BigDecimal inverseRate = BigDecimal.ONE.divide(usdRateFromIDR, 10, RoundingMode.HALF_UP);
        BigDecimal spreadMultiplier = BigDecimal.ONE.add(spreadFactorBD);
        BigDecimal usdBuySpread = inverseRate.multiply(spreadMultiplier);

        // Round to 2 decimal places
        BigDecimal result = usdBuySpread.setScale(2, RoundingMode.HALF_UP);

        log.debug("Calculated USD buy spread: rate={}, spreadFactor={}, result={}",
                usdRateFromIDR, spreadFactor, result);

        return result;
    }
}
