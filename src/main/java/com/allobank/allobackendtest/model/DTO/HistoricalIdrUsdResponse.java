package com.allobank.allobackendtest.model.DTO;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class HistoricalIdrUsdResponse {

    private BigDecimal amount;
    private String base;
    private String start_date;
    private String end_date;
    private Map<String, Map<String, BigDecimal>> rates;

}
