package com.athallah.finance.util.exception;

public class ServerException extends RuntimeException {

    private int code;

    public ServerException() {
        super();
    }

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
