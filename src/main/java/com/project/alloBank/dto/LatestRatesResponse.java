package com.project.alloBank.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LatestRatesResponse {
    private String base;
    private String date;
    private Map<String, Double> rates = new HashMap<>();
    private Double usdBuySpreadIdr;
}
