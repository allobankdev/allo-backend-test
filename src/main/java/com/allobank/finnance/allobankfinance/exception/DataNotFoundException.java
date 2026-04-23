package com.allobank.finnance.allobankfinance.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {

    private String message;

    public DataNotFoundException(String message) {
        super(message);
    }
}
