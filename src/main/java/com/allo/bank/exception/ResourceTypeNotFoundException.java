package com.allo.bank.exception;

public class ResourceTypeNotFoundException extends RuntimeException {

    public ResourceTypeNotFoundException(String resourceType) {
        super("Unsupported resource type: " + resourceType);
    }
}
