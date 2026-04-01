package com.self.bs.source.dto.request;

public class ExchangeRateDataFetcherRequestDto {
    private String dateFrom;
    private String dateTo;
    private String baseCurrency;
    private String targetCurrency;

    public ExchangeRateDataFetcherRequestDto (){
    }

    public ExchangeRateDataFetcherRequestDto (String dateFrom, String dateTo, String baseCurrency, String targetCurrency){
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
    }

    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    public String getBaseCurrency() {
        return baseCurrency;
    }
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
    public String getTargetCurrency() {
        return targetCurrency;
    }
    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @Override
    public String toString() {
        return "ExchangeRateDataFetcherRequestDto [dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", baseCurrency="
                + baseCurrency + ", targetCurrency=" + targetCurrency + "]";
    }
}
