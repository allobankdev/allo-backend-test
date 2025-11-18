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
public class HistoricalRatesResponse {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("rates")
    private Map<LocalDate, Map<String, BigDecimal>> rates;
}
