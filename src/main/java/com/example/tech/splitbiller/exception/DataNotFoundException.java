package com.example.tech.splitbiller.exception;


public class DataNotFoundException extends CaughtException {
    public DataNotFoundException(String message) {
        super(message, 404);
    }
}
