package com.allobank.finance.exception;

public class ResourceTypeNotFoundException extends RuntimeException{

 
    public ResourceTypeNotFoundException(String message) {
        super(message);
    }

    public ResourceTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
