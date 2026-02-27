package com.allobank.finance.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    GENERAL_ERROR("GNR-999", "Internal server error occurred"),
    RESOURCE_NOT_FOUND("GNR-998", "The requested resource was not found in the store"),
    INVALID_INPUT("FIN-001", "Input does not meet API specifications");

    private final String code;
    private final String message;
}
