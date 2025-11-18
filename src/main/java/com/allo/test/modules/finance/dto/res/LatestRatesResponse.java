package com.allo.test.modules.finance.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesResponse {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;
}
