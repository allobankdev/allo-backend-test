package com.self.bs.source.dto.response;

import java.util.Map;

public class HistoryCurrencyRateResponseDto {
    private String amount;
    private String base;
    private String start_date;
    private String end_date;
    private Map<String, Map<String, String>> rates;
    
    public HistoryCurrencyRateResponseDto() {
    }

    public HistoryCurrencyRateResponseDto(String amount, String base, String start_date, String end_date, Map<String, Map<String, String>> rates) {
        this.amount = amount;
        this.base = base;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rates = rates;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public Map<String, Map<String, String>> getRates() {
        return rates;
    }
    public void setRates(Map<String, Map<String, String>> rates) {
        this.rates = rates;
    }
    @Override
    public String toString() {
        return "HistoryCurrencyRateResponseDto [amount=" + amount + ", base=" + base + ", start_date=" + start_date
                + ", end_date=" + end_date + ", rates=" + rates + "]";
    }
}
