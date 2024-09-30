package com.allobank.allobackendtest.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GlobalException extends RuntimeException {

    private String code;
    private HttpStatus httpStatus;

    public GlobalException(String code) {
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
