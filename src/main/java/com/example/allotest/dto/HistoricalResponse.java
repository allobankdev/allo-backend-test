package com.example.allotest.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricalResponse {
    private String base;
    private Map<String, Map<String, Double>> retes;
}
