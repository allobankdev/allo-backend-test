package com.allo.idraggregator.presentation.exception;

public class ResourceTypeNotFoundException extends RuntimeException {

    public ResourceTypeNotFoundException(String resourceType) {
        
        super("Resource type not supported: " + resourceType);
    }
}
