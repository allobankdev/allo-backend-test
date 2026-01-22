package com.allobank.idr_rate_aggregator.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for calculating personalized spread factors.
 * 
 * The spread calculation is based on the GitHub username to ensure
 * each user gets a unique, consistent spread factor.
 * 
 * Formula:
 * 1. Hash username using SHA-256
 * 2. Take hash modulo 8 to get 0-7
 * 3. Spread factor = 0.0032 + (hash_mod_8 * 0.0001)
 * 4. Result: 0.0032 to 0.0039 (0.32% to 0.39%)
 */
@Slf4j
@Component
public class SpreadCalculator {
    
    private static final double BASE_SPREAD = 0.0032; // 0.32%
    private static final double SPREAD_INCREMENT = 0.0001; // 0.01%
    private static final int MODULO = 8;
    
    /**
     * Calculate spread factor based on username.
     * 
     * @param username GitHub username
     * @return spread factor between 0.0032 and 0.0039
     */
    public double calculateSpreadFactor(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Empty username provided, using default spread factor");
            return BASE_SPREAD;
        }
        
        try {
            // Generate SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(username.toLowerCase().getBytes(StandardCharsets.UTF_8));
            
            // Convert first byte to positive int and mod 8
            int hashValue = Math.abs(hash[0]) % MODULO;
            
            // Calculate spread factor
            double spreadFactor = BASE_SPREAD + (hashValue * SPREAD_INCREMENT);
            
            log.debug("Username: {}, Hash value: {}, Spread factor: {}", 
                    username, hashValue, spreadFactor);
            
            return spreadFactor;
            
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            return BASE_SPREAD; // Fallback to base spread
        }
    }
    
    /**
     * Calculate USD buy spread in IDR.
     * 
     * Formula: 1 / (usd_rate * (1 - spread_factor))
     * 
     * @param usdRate USD rate from Frankfurter (e.g., 0.000063)
     * @param spreadFactor spread factor (e.g., 0.0035)
     * @return IDR amount needed to buy 1 USD with spread
     */
    public double calculateUsdBuySpread(double usdRate, double spreadFactor) {
        if (usdRate <= 0) {
            throw new IllegalArgumentException("USD rate must be positive: " + usdRate);
        }
        
        if (spreadFactor < 0 || spreadFactor > 1) {
            throw new IllegalArgumentException("Spread factor must be between 0 and 1: " + spreadFactor);
        }
        
        double buySpread = 1.0 / (usdRate * (1.0 - spreadFactor));
        
        log.debug("USD rate: {}, Spread factor: {}, Buy spread: {}", 
                usdRate, spreadFactor, buySpread);
        
        return buySpread;
    }
}

