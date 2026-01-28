package com.example.demo.dto;

import lombok.Data;
import java.util.Map;

@Data
public class LatestResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;
}
