package com.example.idrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalRatesResponse {

    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates; // date -> (currency -> rate)
}
