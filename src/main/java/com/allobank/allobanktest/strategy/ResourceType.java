package com.allobank.allobanktest.strategy;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ResourceType {

    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    /**
     * Convert raw path variable to ResourceType.
     * Throws IllegalArgumentException if not supported.
     */
    public static ResourceType from(String rawValue) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(rawValue))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported resource type: " + rawValue)
                );
    }

}
