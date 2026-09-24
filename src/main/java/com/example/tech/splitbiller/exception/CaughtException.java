package com.example.tech.splitbiller.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CaughtException extends RuntimeException {
    private final int code;

    public CaughtException(String message, int code) {
        super(message);
        this.code = code;
    }
}
