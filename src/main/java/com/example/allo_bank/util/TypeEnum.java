package com.example.allo_bank.util;

public enum TypeEnum {

    latest_idr_rates("latest_idr_rates"),
    historical_idr_usd("historical_idr_usd"),
    supported_currencies("supported_currencies");

    private final String path;

    TypeEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
