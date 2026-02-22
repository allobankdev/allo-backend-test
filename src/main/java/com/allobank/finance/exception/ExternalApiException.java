package com.allobank.finance.exception;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final int statusCode;

    public ExternalApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
