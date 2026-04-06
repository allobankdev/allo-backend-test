package com.example.allotest.constant;

public enum ResourceType {

    LATEST("latest_idr_rates"),
    HISTORICAL("historical_idr_usd"),
    CURRENCY("supported_currencies");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
