package com.allobank.idr_rate_aggregator.model;

/**
 * Enum untuk tipe resource yang didukung oleh API
 */
public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Konversi string ke enum
     */
    public static ResourceType fromString(String value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Resource type tidak valid: " + value);
    }
}