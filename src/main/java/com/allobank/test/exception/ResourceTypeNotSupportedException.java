package com.allobank.test.exception;

public class ResourceTypeNotSupportedException extends RuntimeException {

    public ResourceTypeNotSupportedException(String message) {
        super(message);
    }
}
