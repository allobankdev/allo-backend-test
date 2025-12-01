package com.app.error;

public class NotFoundError extends RuntimeException{

    public NotFoundError() {
    }

    public NotFoundError(String message) {
        super(message);
    }

    public NotFoundError(String message, Throwable cause) {
        super(message, cause);
    }
}
