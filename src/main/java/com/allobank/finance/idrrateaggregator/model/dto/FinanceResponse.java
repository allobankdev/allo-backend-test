package com.allobank.finance.idrrateaggregator.model.dto;

public class FinanceResponse {

    private String key;
    private Object value;

    public FinanceResponse(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
