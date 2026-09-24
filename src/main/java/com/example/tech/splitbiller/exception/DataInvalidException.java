package com.example.tech.splitbiller.exception;

public class DataInvalidException extends CaughtException {
    public DataInvalidException(String message) {
        super(message, 400);
    }
}
