package com.allobank.enums;

import lombok.Getter;

@Getter
public enum RESPONSE {
    SUCCESS("00", "SUCCESS GET DATA"),
    DATA_NOT_FOUND("40", "DATA NOT FOUND"),
    RESPONSE_DOES_NOT_MATCH("51","SOMETHING MISSING ON EXTERNAL RESPONSE"),
    EXTERNAL_FAILED("50", "SOMETHING WENT WRONG ON EXTERNAL SERVER"),
    GENERAL_ERROR("99", "GENERAL_ERROR");
    private final String code;
    private final String message;

    RESPONSE(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
