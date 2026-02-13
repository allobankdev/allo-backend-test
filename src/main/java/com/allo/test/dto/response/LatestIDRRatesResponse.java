package com.allo.test.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestIDRRatesResponse {

    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
    private BigDecimal USD_BuySpread_IDR;
}
