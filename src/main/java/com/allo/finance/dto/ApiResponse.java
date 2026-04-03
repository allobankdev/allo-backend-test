package com.allo.finance.dto;

public class ApiResponse {

    private String resourceType;
    private Object data;

    public ApiResponse(String resourceType, Object data) {
        this.resourceType = resourceType;
        this.data = data;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Object getData() {
        return data;
    }
}