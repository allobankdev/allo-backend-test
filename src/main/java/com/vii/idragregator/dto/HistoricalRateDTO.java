package com.vii.idragregator.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HistoricalRateDTO {
    private String base;
    private String start_date;
    private String end_date;
    private Map<String, Map<String, Double>> rates;
}
