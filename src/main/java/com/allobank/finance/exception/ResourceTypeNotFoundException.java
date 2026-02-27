package com.allobank.finance.exception;

public class ResourceTypeNotFoundException extends RuntimeException {

    public ResourceTypeNotFoundException(String resourceType) {
        super("ResourceType '" + resourceType + "' not found");
    }
}