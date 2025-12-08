package com.example.allobank.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalIdrUsd {
    private String startDate;
    private String endDate;
    private Map<String, Double> rates;       
}
