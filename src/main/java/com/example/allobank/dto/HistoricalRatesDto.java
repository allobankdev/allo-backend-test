package com.example.allobank.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class HistoricalRatesDto {
    private BigDecimal amount;
    private String base;

    // Frankfurter uses start_date & end_date in timeseries response
    private String start_date;
    private String end_date;

    /**
     * Example:
     * rates: {
     *  "2024-01-01": {"USD": 0.000064},
     *  "2024-01-02": {"USD": 0.000065}
     * }
     */
    private Map<String, Map<String, BigDecimal>> rates;
}