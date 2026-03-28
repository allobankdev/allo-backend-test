package com.allobank.allo_backend_test.finance.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotSupportedException extends AppException {
    public ResourceNotSupportedException(String message) {
        super(message, HttpStatus.NOT_FOUND, "resource not supported");
    }
}