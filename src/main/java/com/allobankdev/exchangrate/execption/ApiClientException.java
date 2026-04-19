package com.allobankdev.exchangrate.execption;

import lombok.Getter;

@Getter
public class ApiClientException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public ApiClientException(int statusCode, String responseBody, String message) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }


}
