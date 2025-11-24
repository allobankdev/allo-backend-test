package com.test.allo_bank_test_exhange_rate.enums;

public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String resourceType;

    private ResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String toString() {
        return String.valueOf(resourceType);
    }
}
