package com.bezkoder.springjwt.payload;

import java.time.Instant;

public class ApiErrorResponse {
    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
