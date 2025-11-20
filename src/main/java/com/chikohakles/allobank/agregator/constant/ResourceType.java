package com.chikohakles.allobank.agregator.constant;

import lombok.Getter;

@Getter
public enum ResourceType {
    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies"),
    ;

    final String code;
    ResourceType(String code) {
        this.code = code;
    }

    public static ResourceType fromCode(String code) {
        for (ResourceType rt : values()) {
            if (rt.code.equalsIgnoreCase(code)) {
                return rt;
            }
        }
        throw new IllegalArgumentException("Unknown resourceType: " + code);
    }
}
