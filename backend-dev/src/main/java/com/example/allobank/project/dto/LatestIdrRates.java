package com.example.allobank.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestIdrRates {
    private String currency;                     
    private String date;                    
    private Map<String, Double> rates;       
    private double usdBuySpreadIdr;     
}