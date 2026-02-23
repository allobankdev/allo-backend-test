package com.allo.backendtest.exception;

public class UnknownResourceException extends RuntimeException {
    public UnknownResourceException(String resourceType) {
        super("Unknown resource type: " + resourceType);
    }
}