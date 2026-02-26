package com.api.allorestapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the three valid {resourceType} path values accepted by
 * GET /api/finance/data/{resourceType}.
 */
public enum ResourceType {

    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static ResourceType fromString(String raw) {
        for (ResourceType type : values()) {
            if (type.value.equalsIgnoreCase(raw)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown resourceType: '" + raw +
                "'. Valid values: latest_idr_rates, historical_idr_usd, supported_currencies");
    }
}
