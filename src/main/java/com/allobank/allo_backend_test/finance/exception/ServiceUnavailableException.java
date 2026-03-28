package com.allobank.allo_backend_test.finance.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends AppException {
    public ServiceUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, "service unavailable");
    }
}