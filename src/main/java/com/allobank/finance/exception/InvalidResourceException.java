package com.allobank.finance.exception;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String resourceType) {
        super("Invalid resource type " + resourceType);
    }
}
