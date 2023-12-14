package com.allobank.allobackendtest.app;

import java.util.List;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppResponse<T> {

    @JsonProperty("responseCode")
    private Number responseCode;

    @JsonProperty("responseMessage")
    private String message;

    @JsonProperty("responseError")
    private String error;

    @JsonProperty("data")
    private List<Caleg> data;

    public Number getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Caleg> getData() {
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

    public void setData(List<Caleg> dataList) {
        this.data = dataList;
    }

    public void setErrorMessage(String error) {
        this.error = error;
    }

}
