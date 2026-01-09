package com.example.idr.model;

public class CurrencyResult {

    private String code;
    private String name;

    public CurrencyResult(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
