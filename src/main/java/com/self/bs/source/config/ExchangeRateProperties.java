package com.self.bs.source.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bs.exchange-rate")
public class ExchangeRateProperties {
    private String cacheName;
    private String externalUrl;
    private String baseCurrency;
    private String targetCurrency;
    private int defaultHistoricalRangeDate = 7;
    private String dateFormat = "yyyy-MM-dd";
    private String rangeDateSeparator = "..";
    private String personalName;

    public String getCacheName() {
        return cacheName;
    }
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
    public String getExternalUrl() {
        return externalUrl;
    }
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
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
    public int getDefaultHistoricalRangeDate() {
        return defaultHistoricalRangeDate;
    }
    public void setDefaultHistoricalRangeDate(int defaultHistoricalRangeDate) {
        this.defaultHistoricalRangeDate = defaultHistoricalRangeDate;
    }
    public String getDateFormat() {
        return dateFormat;
    }
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    public String getRangeDateSeparator() {
        return rangeDateSeparator;
    }
    public void setRangeDateSeparator(String rangeDateSeparator) {
        this.rangeDateSeparator = rangeDateSeparator;
    }
    public String getPersonalName() {
        return personalName;
    }
    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }
}
