package com.allo.finance.model;

import java.math.BigDecimal;
import java.util.Map;

public class HistoricalRateResponse {

    private String startDate;
    private String endDate;
    private String base;
    private Map<String, Map<String, BigDecimal>> rates;

    public HistoricalRateResponse() {}

    public HistoricalRateResponse(
            String startDate,
            String endDate,
            String base,
            Map<String, Map<String, BigDecimal>> rates
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.base = base;
        this.rates = rates;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getBase() {
        return base;
    }

    public Map<String, Map<String, BigDecimal>> getRates() {
        return rates;
    }
}
