package com.allo.backendtest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

}