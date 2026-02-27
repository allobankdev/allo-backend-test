package com.allobank.finance.enums;

import com.allobank.finance.exception.ResourceTypeNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResourceType {

    LATEST_IDR_RATES("latest_idr_rates"),
    HISTORICAL_IDR_USD("historical_idr_usd"),
    SUPPORTED_CURRENCIES("supported_currencies");

    private final String value;

    public static ResourceType from(String value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ResourceTypeNotFoundException(value);
    }
}