package com.example.frankfurter.dto;

public class CurrencyDto {

    private String code;
    private String description;

    public CurrencyDto(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
