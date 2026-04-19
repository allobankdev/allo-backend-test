package com.allobankdev.exchangrate.constant;

import lombok.Getter;

@Getter
public enum ResourceType {
    SUPPORTED_CURRENCIES("supported_currencies"),
    HISTORICAL_RATES("historical_idr_usd"),
    LATEST_RATES("latest_idr_rates");


    private final String name;

    ResourceType(String name) {
        this.name = name;
    }

    public static ResourceType getFromName(String name) {
        for (ResourceType value : ResourceType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
