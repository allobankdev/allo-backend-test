package com.allobank.allobackendtest.model.DTO;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class LatestIdrRatesResponse {

    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private BigDecimal usdBuySpreadIdr;

}
