package com.allo.splitbill.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private int status;
    private List<String> errors;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, List<String> errors) {
        this.status = status;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
