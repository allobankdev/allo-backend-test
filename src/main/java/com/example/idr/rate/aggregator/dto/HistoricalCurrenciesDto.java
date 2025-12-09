package com.example.idr.rate.aggregator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class HistoricalCurrenciesDto {
    private String startDate;
    private String endDate;
    private Map<String, Object> raw;
}
