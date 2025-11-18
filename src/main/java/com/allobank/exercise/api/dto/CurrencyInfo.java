package com.allobank.exercise.api.dto;

public class CurrencyInfo {
    private String code;
    private String name;

    public CurrencyInfo(String key, String value) {
        this.code = key;
        this.name = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
