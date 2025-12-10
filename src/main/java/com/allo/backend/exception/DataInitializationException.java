package com.allo.backend.exception;

public class DataInitializationException extends RuntimeException {

    public DataInitializationException(String message) {
        super(message);
    }

    public DataInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
