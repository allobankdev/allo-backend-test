package com.allobank.idr_rate_aggregator.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO untuk response latest rates dari Frankfurter API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestRatesResponse {
    
    private BigDecimal amount;
    
    private String base;
    
    private LocalDate date;
    
    private Map<String, BigDecimal> rates;
    
    /**
     * Field khusus untuk USD buy spread yang dihitung dari GitHub username
     * Field ini TIDAK ada di API, akan dihitung oleh aplikasi kita
     */
    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIdr;
}