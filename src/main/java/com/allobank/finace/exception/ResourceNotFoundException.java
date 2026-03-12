package com.allobank.finace.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType) {
        super("Unknown resource type: '" + resourceType + "'. Valid types are: latest_idr_rates, historical_idr_usd, supported_currencies");
    }
}
