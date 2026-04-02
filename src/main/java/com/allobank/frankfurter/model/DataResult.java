package com.allobank.frankfurter.model;

public class DataResult {
    private final String resourceType;
    private final Object data; // can be Map, List, etc.

    public DataResult(String resourceType, Object data) {
        this.resourceType = resourceType;
        this.data = data;
    }

    public String getResourceType() { return resourceType; }
    public Object getData() { return data; }
}