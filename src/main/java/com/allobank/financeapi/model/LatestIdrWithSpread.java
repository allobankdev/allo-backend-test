package com.allobank.financeapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LatestIdrWithSpread {
    private String currency;
    private BigDecimal originalRate;
    private BigDecimal usdBuySpreadIdr; // IDR rate with bank spread applied
    private BigDecimal spreadFactor; // Unique factor from GitHub username
    private LocalDate date;
}