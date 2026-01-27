package com.hend.backend.exception;

/**
 * @author : hend wunga
 */

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType) {
        super("Resource not found: " + resourceType);
    }
}

