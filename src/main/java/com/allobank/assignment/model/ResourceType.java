package com.allobank.assignment.model;

import java.util.Arrays;

public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ResourceType from(String raw) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(raw))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown resourceType: " + raw));
    }
}
