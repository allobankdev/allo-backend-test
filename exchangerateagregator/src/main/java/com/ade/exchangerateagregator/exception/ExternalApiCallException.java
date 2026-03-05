package com.ade.exchangerateagregator.exception;

public class ExternalApiCallException extends RuntimeException{
    public ExternalApiCallException(String message){
        super(message);
    }

    public ExternalApiCallException(String message, Throwable throwable){
        super(message,throwable);
    }
}
