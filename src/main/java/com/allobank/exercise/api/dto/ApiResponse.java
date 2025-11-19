package com.allobank.exercise.api.dto;

import java.time.Instant;

public class ApiResponse<T> {
    private String status;
    private String timestamp;
    private T data;

    public ApiResponse(){
        this.timestamp = Instant.now().toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
