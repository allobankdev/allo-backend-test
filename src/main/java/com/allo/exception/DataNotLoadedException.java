package com.allo.exception;

public class DataNotLoadedException extends RuntimeException {

    public DataNotLoadedException() {
        super("Data has not been loaded yet. Please try again shortly.");
    }
}
