package com.allo.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LatestRateDto {

    private String currency;
    private BigDecimal rate;
    private BigDecimal usdBuyRate;

}