package com.allo.finance.model;

import java.math.BigDecimal;
import java.util.Map;

public class LatestRateResponse {

    private String base;
    private Map<String, BigDecimal> rates;

    // custom calculated field
    private BigDecimal usdBuySpreadIdr;

    public LatestRateResponse() {}

    public LatestRateResponse(
            String base,
            Map<String, BigDecimal> rates,
            BigDecimal usdBuySpreadIdr
    ) {
        this.base = base;
        this.rates = rates;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getBase() {
        return base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public BigDecimal getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }
}
