package com.hanifnfl.allobank.exception;

public class DataNotAvailableException extends RuntimeException {
    public DataNotAvailableException(String resourceType) {
        super("Data not available for resourceType: " + resourceType);
    }
}
