package com.allobank.finance.exception;

import lombok.Getter;

@Getter
public class WebClientException extends RuntimeException {
    private final int status;

    public WebClientException(String message, int status) {
        super(message);
        this.status = status;
    }
}
