package com.musfiul.idrrateaggregator.constant;

import com.musfiul.idrrateaggregator.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum ResourceType {

    LATEST_IDR_RATES,
    HISTORICAL_IDR_USD,
    SUPPORTED_CURRENCIES;

    public static ResourceType from(String value) {
        try {
            return ResourceType.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BadRequestException("Invalid resource type: " + value);
        }
    }
}
