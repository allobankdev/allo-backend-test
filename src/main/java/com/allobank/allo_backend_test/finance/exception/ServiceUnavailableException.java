package com.allobank.allo_backend_test.finance.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) { super(message); }
}