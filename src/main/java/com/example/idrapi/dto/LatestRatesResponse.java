package com.example.idrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestRatesResponse {

    private String base;
    private String date;
    private Map<String, Double> rates = new HashMap<>();
}
