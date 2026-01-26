package com.hend.backend.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author : hend wunga
 */

@Data
public class FrankfurterResponse {
    private double amount;
    private String base;
    private String date;
    private Map<String,Double> rates;
}
