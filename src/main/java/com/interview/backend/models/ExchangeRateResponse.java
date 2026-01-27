package com.interview.backend.models;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRateResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
