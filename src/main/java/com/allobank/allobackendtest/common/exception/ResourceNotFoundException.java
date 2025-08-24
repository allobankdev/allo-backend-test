package com.allobank.allobackendtest.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException class for handling resource not found errors.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // otomatis kasih status 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
