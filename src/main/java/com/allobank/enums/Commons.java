package com.allobank.enums;

import lombok.Getter;

@Getter
public enum Commons {
    USD("USD"),
    STATUS_FAILED("Failed"),
    STATUS_SUCCESS("success"),
    ACCEPT("Accept");
    private final String value;
    Commons(String value) {
        this.value = value;
    }
}
