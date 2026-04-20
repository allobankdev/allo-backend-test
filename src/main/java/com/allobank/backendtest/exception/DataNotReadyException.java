package com.allobank.backendtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class DataNotReadyException extends RuntimeException {
    public DataNotReadyException(String message) {
        super(message);
    }
}
