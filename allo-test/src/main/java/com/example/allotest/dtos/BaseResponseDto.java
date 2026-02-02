package com.example.allotest.dtos;

import java.time.Instant;

public class BaseResponseDto<T> {
    private String code;
    private String message;
    private Instant timestamp;
    private String resourceType;
    private T data;

    public BaseResponseDto(String code, String message, String resourceType, T data) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
        this.resourceType = resourceType;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
