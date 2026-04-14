package com.thasya.frankfurter.exception;

public class UnknownResourceTypeException extends RuntimeException {
    public UnknownResourceTypeException(String resourceType) {
        super("Unknown resource type: " + resourceType);
    }
}
