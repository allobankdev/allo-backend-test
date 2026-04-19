package com.allobankdev.exchangrate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class LatestRateResponse {
    private int amount;
    private String base;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private java.util.Map<String, BigDecimal> rates;
    private java.math.BigDecimal usdBuySpreadIdr;
}
