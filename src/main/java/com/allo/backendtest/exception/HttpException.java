package com.allo.backendtest.exception;

import lombok.Getter;

@Getter
public class HttpException extends Exception {
    private final Integer errorCode;
    public HttpException(Integer errorCode,String message) {
        this.errorCode = errorCode;
        super(message);
    }
}