package com.app.error;

public class ServerStatusException extends RuntimeException{
    public ServerStatusException() {
    }

    public ServerStatusException(String message) {
        super(message);
    }

    public ServerStatusException(String message, Throwable cause) {
        super(message, cause);
    }

}
