package com.allo.bank.exception;

public class DataNotInitializedException extends RuntimeException {

    public DataNotInitializedException(String resourceType) {
        super("Data is not initialized for resource type: " + resourceType);
    }
}
