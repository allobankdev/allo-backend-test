package com.allo.idr.enums;

import com.allo.idr.exception.ExternalApiException;

public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String key;

    ResourceType(String key) {
        this.key = key;
    }

    public static ResourceType from(String raw) {
        for (ResourceType val : values()){
            if (val.key.equalsIgnoreCase(raw)){
                return val;
            }
        }
        throw new ExternalApiException("Unknown resourceType" + raw);
    }
}
