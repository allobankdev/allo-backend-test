package com.allobank.exercise.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ResourceType {

    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String path;

    ResourceType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @JsonCreator
    public static ResourceType fromPath(String path) {
        for (ResourceType t : values()) {
            if (t.path.equalsIgnoreCase(path)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown resource type: " + path);
    }
}
