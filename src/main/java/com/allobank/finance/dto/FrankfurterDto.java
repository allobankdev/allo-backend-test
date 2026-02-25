package com.allobank.finance.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class FrankfurterDto {

    @Data
    @NoArgsConstructor
    public static class LatestRatesResponse {
        private String amount;
        private String base;
        private String date;
        private Map<String, BigDecimal> rates = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @JsonPropertyOrder({"amount", "base", "start_date", "end_date", "rates"})
    public static class HistoricalRatesResponse {
        private String amount;
        private String base;

        @JsonProperty("start_date")
        private String startDate;

        @JsonProperty("end_date")
        private String endDate;

        private Map<String, Map<String, BigDecimal>> rates = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    public static class CurrenciesResponse {
        private Map<String, String> currencies = new LinkedHashMap<>();

        @JsonAnySetter
        public void addCurrency(String key, String value) {
            this.currencies.put(key, value);
        }

        @JsonAnyGetter
        public Map<String, String> getCurrencies() {
            return currencies;
        }
    }
}