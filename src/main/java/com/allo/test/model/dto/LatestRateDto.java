package com.allo.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LatestRateDto {

    private String currency;
    private Double rate;
    private Double usdBuyRate;

}