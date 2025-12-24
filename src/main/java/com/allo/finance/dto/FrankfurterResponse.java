package com.allo.finance.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FrankfurterResponse {

    private double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

}