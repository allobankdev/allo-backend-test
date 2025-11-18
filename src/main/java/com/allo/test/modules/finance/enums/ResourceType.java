package com.allo.test.modules.finance.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the types of resources stored in the data store.
 * <p>
 * Keys match the @Component bean names for consistency.
 */
@Getter
@RequiredArgsConstructor
public enum ResourceType {
    LATEST_RATES("latest_idr_rates", "latest IDR rates with USD buy spread"),
    HISTORICAL_RATES("historical_idr_usd", "historical rates"),
    CURRENCIES("supported_currencies", "currencies");

    private final String key;
    private final String description;

    /**
     * Converts a string key to its corresponding ResourceType enum.
     *
     * @param key the string key (e.g., "latest_idr_rates")
     * @return the matching ResourceType, or null if not found
     */
    public static ResourceType fromString(String key) {
        if (key == null) {
            return null;
        }

        for (ResourceType type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }

        return null;
    }

}
