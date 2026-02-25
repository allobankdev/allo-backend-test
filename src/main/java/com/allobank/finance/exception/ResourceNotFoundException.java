package com.allobank.finance.exception;

/**
 * Exception yang dilempar ketika resourceType yang diminta tidak ditemukan
 * di in-memory store (misalnya resource type tidak dikenal).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType) {
        super("Resource type tidak ditemukan: '" + resourceType + "'. " +
                "Nilai yang valid: latest_idr_rates, historical_idr_usd, supported_currencies");
    }
}
