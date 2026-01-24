package com.sdewa.IdrRateAggregator.dtoes;

import java.math.BigDecimal;

// import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalIdrUsdResponseRecord {
    private double amount;
    private String base;
    private String startDate;
    private String endDate;
    private String date;
    private String currency;
    private BigDecimal rates;
}
