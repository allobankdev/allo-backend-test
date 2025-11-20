package com.allobank.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ExternalServiceException extends RuntimeException {
    private final HttpStatusCode status;

    public ExternalServiceException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public ExternalServiceException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
