package com.allobank.allobackendtest.app;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppResponse<T> {

    @JsonProperty("responseCode")
    private Number responseCode;

    @JsonProperty("responseMessage")
    private String message;

    @JsonProperty("responseError")
    private String error;

    @JsonProperty("data")
    private T data;

    public Number getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public void setCode(Number number) {
        this.responseCode = number;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorMessage(String error) {
        this.error = error;
    }

}
