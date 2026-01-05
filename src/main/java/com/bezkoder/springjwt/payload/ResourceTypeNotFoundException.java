package com.bezkoder.springjwt.payload;

public class ResourceTypeNotFoundException extends RuntimeException {
    public ResourceTypeNotFoundException(String resourceType) {
        super("Unknown resourceType: " + resourceType);
    }
}
