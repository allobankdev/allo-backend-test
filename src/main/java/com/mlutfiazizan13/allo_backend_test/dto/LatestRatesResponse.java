package com.mlutfiazizan13.allo_backend_test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LatestRatesResponse {

    private BigDecimal amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIdr;
}
