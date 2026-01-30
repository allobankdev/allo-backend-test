package com.example.allobank.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LatestRatesResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
}