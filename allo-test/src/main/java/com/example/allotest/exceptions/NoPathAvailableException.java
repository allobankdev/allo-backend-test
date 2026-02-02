package com.example.allotest.exceptions;

public class NoPathAvailableException extends RuntimeException {
    private final String resourceType;

    public NoPathAvailableException(String message, String resourceType) {
        super(message);
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }
}
