package com.aryaevan.allo.util;

/**
 * Utility class for spread factor and USD buying spread calculations.
 * This utility is independent of any service layer dependencies.
 */
public class SpreadCalculationUtil {
    
    /**
     * Calculates the spread factor based on the GitHub username.
     * Formula: (Sum of Unicode Values % 1000) / 100000.0
     * 
     * @param username The GitHub username
     * @return The calculated spread factor (between 0.00000 and 0.00999)
     */
    public static double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();
        
        return (sum % 1000) / 100000.0;
    }

    /**
     * Calculates the USD buying spread rate for IDR.
     * Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     * 
     * @param rateUsd The USD to IDR exchange rate from API
     * @param spreadFactor The calculated spread factor
     * @return The USD buying spread rate
     */
    public static double calculateUsdBuySpreadIdR(double rateUsd, double spreadFactor) {
        return (1.0 / rateUsd) * (1.0 + spreadFactor);
    }
}
