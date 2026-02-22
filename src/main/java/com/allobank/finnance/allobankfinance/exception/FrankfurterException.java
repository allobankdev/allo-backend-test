package com.allobank.finnance.allobankfinance.exception;

import lombok.Getter;

@Getter
public class FrankfurterException extends RuntimeException {
    private String message;

    public FrankfurterException(String message) {
        super(message);
    }
}

