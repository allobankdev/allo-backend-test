package com.example.allotest.exceptions;

public class CustomGlobalException extends RuntimeException {
    private final String resourceType;

    public CustomGlobalException(String message, String resourceType) {
        super(message);
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }
}
