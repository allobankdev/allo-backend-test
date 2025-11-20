package com.finance.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rates {

        private Map<String, Double> values;
    }
}

