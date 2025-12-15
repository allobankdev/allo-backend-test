package com.allobank.frankfurter_aggregator.dto;

import java.time.LocalDate;
import java.util.Map;

import lombok.Data;

@Data
public class HistoricalData {
    private String from;
    private String to;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<LocalDate, Double> rates;
}
