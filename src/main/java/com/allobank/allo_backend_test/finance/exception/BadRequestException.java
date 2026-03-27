package com.allobank.allo_backend_test.finance.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}