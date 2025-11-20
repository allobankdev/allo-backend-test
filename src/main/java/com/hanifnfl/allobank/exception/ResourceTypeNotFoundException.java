package com.hanifnfl.allobank.exception;

public class ResourceTypeNotFoundException extends RuntimeException {
    public ResourceTypeNotFoundException(String resourceType) {
        super("Unknown resourceType: " + resourceType);
    }
}
