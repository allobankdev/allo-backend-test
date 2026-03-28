package com.allobank.allo_backend_test.finance.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String error;

    public AppException(String message, HttpStatus status, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }
}