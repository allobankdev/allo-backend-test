package com.example.frankfurter.exception;

public class DataNotLoadedException extends RuntimeException {
    public DataNotLoadedException(String message) {
        super(message);
    }
}
