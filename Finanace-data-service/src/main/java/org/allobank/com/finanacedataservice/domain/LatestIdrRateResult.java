package org.allobank.com.finanacedataservice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LatestIdrRateResult {

    private final String base;
    private final LocalDate date;
    private final BigDecimal rateUsd;
    private final BigDecimal usdBuySpreadIdr;
    private final BigDecimal spreadFactor;

    public LatestIdrRateResult(String base, LocalDate date, BigDecimal rateUsd, BigDecimal usdBuySpreadIdr, BigDecimal spreadFactor) {
        this.base = base;
        this.date = date;
        this.rateUsd = rateUsd;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
        this.spreadFactor = spreadFactor;
    }


    public String getBase() {
        return base;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getRateUsd() {
        return rateUsd;
    }

    public BigDecimal getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }

    public BigDecimal getSpreadFactor() {
        return spreadFactor;
    }


}
