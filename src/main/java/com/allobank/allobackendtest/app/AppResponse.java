package com.allobank.allobackendtest.app;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppResponse<T> {
    
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

}
