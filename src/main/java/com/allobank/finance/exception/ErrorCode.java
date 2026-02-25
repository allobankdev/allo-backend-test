package com.allobank.finance.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // General errors (1xxx)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "Internal server error occurred"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 1001, "Validation failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 1002, "Invalid request"),

    // Resource errors (3xxx)
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 3000, "Resource not found"),

    // Cache/State errors (5xxx)
    CACHE_NOT_INITIALIZED(HttpStatus.SERVICE_UNAVAILABLE, 5000, "Cache not yet initialized"),
    CACHE_ALREADY_INITIALIZED(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "Cache already initialized"),
    CACHE_IMMUTABLE(HttpStatus.INTERNAL_SERVER_ERROR, 5002, "Cache is immutable after initialization")

    ;

    private final HttpStatus status;
    private final Integer code;
    private final String message;

    public BaseException toException(String customMessage) {
        String message = this.message + (customMessage != null ? ". " + customMessage : "");
        return new BaseException(this, message);
    }

    public BaseException toException() {
        return new BaseException(this, message);
    }
}
