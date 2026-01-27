package com.hend.backend.exception;

/**
 * @author : hend wunga
 */

public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }
}
