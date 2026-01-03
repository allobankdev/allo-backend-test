package com.allobank.allobackendtest.model.DTO;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class HistoricalIdrUsdResponse {

    private BigDecimal amount;
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, BigDecimal>> rates;

}
