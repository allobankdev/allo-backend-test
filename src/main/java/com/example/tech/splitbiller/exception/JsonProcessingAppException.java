package com.example.tech.splitbiller.exception;


public class JsonProcessingAppException extends CaughtException {

    public JsonProcessingAppException(String message) {
        super(message, 500);
    }
}
