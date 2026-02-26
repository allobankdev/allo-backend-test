package org.allobank.com.finanacedataservice.clent.dto;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class FrankfurterTimeSeriesResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private String base;
    private Map<LocalDate, Map<String, BigDecimal>> rates;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<LocalDate, Map<String, BigDecimal>> getRates() {
        return rates;
    }

    public void setRates(Map<LocalDate, Map<String, BigDecimal>> rates) {
        this.rates = rates;
    }
}
