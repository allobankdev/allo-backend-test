package com.allobank.idr_rate_aggregator.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO untuk response historical rates dari Frankfurter API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricalRatesResponse {
    
    private BigDecimal amount;
    
    private String base;
    
    @JsonIgnoreProperties
    private LocalDate start_date;
    
    @JsonIgnoreProperties
    private LocalDate end_date;
    
    /**
     * Map dengan key = tanggal, value = Map rates
     * Contoh: {"2024-01-01": {"USD": 0.000064}, "2024-01-02": {"USD": 0.000065}}
     */
    private Map<String, Map<String, BigDecimal>> rates;
}