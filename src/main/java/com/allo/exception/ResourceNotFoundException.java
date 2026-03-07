package com.allo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType) {
        super("Unknown resource type: " + resourceType);
    }
}
